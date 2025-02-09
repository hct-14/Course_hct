package Course.demo.Service;


import Course.demo.Dto.Request.CreateCourseReq;
import Course.demo.Dto.Request.UpdateCourseReq;
import Course.demo.Dto.Response.CourseResponse;
import Course.demo.Dto.Response.Page.ResultPaginationDTO;
import Course.demo.Dto.Response.ProveUserResponse;
import Course.demo.Dto.Response.UserResponse;
import Course.demo.Entity.Course;
import Course.demo.Entity.User;
import Course.demo.Entity.UserCourse;
import Course.demo.Mapper.CourseMapper;
import Course.demo.Repository.CourseRepository;
import Course.demo.Repository.UserCourseRepository;
import Course.demo.Repository.UserRepository;
import Course.demo.Util.SecurityUtil;
import Course.demo.Util.error.IdInvaldException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private CourseRepository courseRepository;
    private CourseMapper courseMapper;
    private UserRepository userRepository;
    private UserCourseRepository userCourseRepository;
    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, UserRepository userRepository, UserCourseRepository userCourseRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.userRepository = userRepository;
        this.userCourseRepository = userCourseRepository;
    }

    public Course createCourse(CreateCourseReq courseRequest) {
        // Chuyển đổi từ DTO sang thực thể Course
        Course course = courseMapper.toCourse(courseRequest);
        if (course == null) {
            throw new RuntimeException("Error converting courseRequest to Course");
        }
        // Lấy email của người dùng hiện tại
        String currentUserEmail = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User is not logged in"));
        // Tìm người dùng theo email
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found with email: " + currentUserEmail);
        }
        // Khởi tạo danh sách UserCourses nếu chưa có
        if (course.getUserCourses() == null) {
            course.setUserCourses(new ArrayList<>());
        }
        // Kiểm tra xem người dùng đã liên kết với khóa học này chưa
        boolean isUserAlreadyLinked = course.getUserCourses().stream()
                .anyMatch(userCourse -> userCourse.getUser().equals(currentUser));
        if (isUserAlreadyLinked) {
            throw new RuntimeException("User is already linked to this course");
        }

        // Tạo đối tượng UserCourse và thiết lập thông tin
        UserCourse userCourse = new UserCourse();
        userCourse.setUser(currentUser);
        userCourse.setCourse(course);
        userCourse.setTeacher(true); // Đặt mặc định là giáo viên nếu cần

        // Thêm UserCourse vào danh sách UserCourses của khóa học
        course.getUserCourses().add(userCourse);

        // Lưu khóa học và trả về kết quả
        return courseRepository.save(course);
    }

    public Course fetchById(int id) {
        Optional<Course> course = courseRepository.findById(id);
        if (!course.isPresent()) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        return course.get();
    }

    public Course updateCourse(UpdateCourseReq courseReq)throws IdInvaldException {
        Course course = courseMapper.toCourseUpdate(courseReq);

        Optional<Course> optionalCourse = courseRepository.findById(course.getId());
        if (!optionalCourse.isPresent()) {
            throw new IdInvaldException("Course not found with id: " + course.getId());
        }
        Course updatedCourse = optionalCourse.get();
        // Cập nhật các trường có thể thay đổi
        if (courseReq.getName() != null) {
            updatedCourse.setName(courseReq.getName());
        }
        if (courseReq.getDescription() != null) {
            updatedCourse.setDescription(courseReq.getDescription());
        }
        if (courseReq.getDescriptionName() != null) {
            updatedCourse.setDescriptionName(courseReq.getDescriptionName());
        }
        if (courseReq.getProvide() != null) {
            updatedCourse.setProvide(courseReq.getProvide());
        }
        if (courseReq.getRequest() != null) {
            updatedCourse.setRequest(courseReq.getRequest());
        }
        if (courseReq.getRating() != 0) { // Giả sử rating mặc định là 0
            updatedCourse.setRating(courseReq.getRating());
        }

//        // Cập nhật userCourses (nếu có)
//        if (courseReq.getUserCourses() != null && !courseReq.getUserCourses().isEmpty()) {
//            updatedCourse.setUserCourses(courseReq.getUserCourses());
//        }

        return courseRepository.save(updatedCourse);

    }
    public ResultPaginationDTO fetchAllCourses(Specification<Course> specification, Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(specification, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(courses.getTotalPages());
        mt.setTotal(courses.getTotalElements());
        res.setMeta(mt);
        List<CourseResponse> courseList = courses.getContent().stream().map(
                item-> this.convertToCourseResponse(item)
        ).collect(Collectors.toList());
        res.setResult(courseList);
        return res;
    }
    ///Khoá học đã mua
    public ResultPaginationDTO fetchAllCoursesById(Specification<Course> specification, Pageable pageable) {
        // Lấy email của người dùng hiện tại
        String currentUserEmail = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User is not logged in"));

        // Tìm người dùng theo email
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found with email: " + currentUserEmail);
        }

        // Lấy danh sách courseId của user từ bảng UserCourse
        List<Integer> enrolledCourseIds = userCourseRepository.findCourseIdsByUserId(currentUser.getId());

        // Lọc chỉ các khóa học mà user đã đăng ký
        Specification<Course> userCoursesSpec = (root, query, cb) -> root.get("id").in(enrolledCourseIds);
        Specification<Course> finalSpec = specification.and(userCoursesSpec);

        // Lấy danh sách khóa học theo điều kiện
        Page<Course> courses = courseRepository.findAll(finalSpec, pageable);

        // Tạo đối tượng kết quả
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(courses.getTotalPages());
        mt.setTotal(courses.getTotalElements());
        res.setMeta(mt);

        // Chuyển đổi danh sách Course sang CourseResponse
        List<CourseResponse> courseList = courses.getContent().stream()
                .map(this::convertToCourseResponse)
                .collect(Collectors.toList());

        res.setResult(courseList);
        return res;
    }

    public ResultPaginationDTO fetchAllCoursesByTeacher(Specification<Course> specification, Pageable pageable) {
        // Lấy email của người dùng hiện tại
        String currentUserEmail = SecurityUtil.getCurrentUserLogin()
                .orElseThrow(() -> new RuntimeException("User is not logged in"));

        // Tìm người dùng theo email
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new RuntimeException("User not found with email: " + currentUserEmail);
        }

        // Lấy danh sách courseId mà user là giảng viên (isTeacher = true)
        List<Integer> teacherCourseIds = userCourseRepository.findTeacherCourseIdsByUserId(currentUser.getId());

        // Nếu không có khóa học nào, trả về danh sách rỗng
        if (teacherCourseIds.isEmpty()) {
            return null;
        }

        // Lọc chỉ các khóa học mà user là giảng viên
        Specification<Course> teacherCoursesSpec = (root, query, cb) -> root.get("id").in(teacherCourseIds);
        Specification<Course> finalSpec = specification.and(teacherCoursesSpec);

        // Lấy danh sách khóa học theo điều kiện
        Page<Course> courses = courseRepository.findAll(finalSpec, pageable);

        // Tạo đối tượng kết quả
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(courses.getTotalPages());
        mt.setTotal(courses.getTotalElements());
        res.setMeta(mt);

        // Chuyển đổi danh sách Course sang CourseResponse
        List<CourseResponse> courseList = courses.getContent().stream()
                .map(this::convertToCourseResponse)
                .collect(Collectors.toList());

        res.setResult(courseList);
        return res;
    }



    public void deleteCourse(int id) {
        Optional<Course> course = courseRepository.findById(id);
        if (!course.isPresent()) {
            throw new RuntimeException("Course not found with id: " + id);

        }
        courseRepository.deleteById(id);
    }
    public CourseResponse convertToCourseResponse(Course course) {
        // Lấy danh sách UserResponse từ UserCourse chỉ khi isTeacher = true
        List<UserResponse> userResponses = course.getUserCourses().stream()
                .filter(userCourse -> userCourse.isTeacher()) // Lọc các UserCourse có isTeacher = true
                .map(userCourse -> new UserResponse(
                        userCourse.getUser().getId(),
                        userCourse.getUser().getName()
                )) // Chuyển User thành UserResponse
                .collect(Collectors.toList());

        return new CourseResponse(
                course.getId(),
                course.getName(),
                course.getDescriptionName(),
                course.getDescription(),
                course.getProvide(),
                course.getRequest(),
                course.getRating(),
                course.getPrice(),
                course.getSkyHighPrices(),
                userResponses
        );
    }


}

