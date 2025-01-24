package Course.demo.Service;


import Course.demo.Dto.Request.CreateCourseReq;
import Course.demo.Dto.Response.CourseResponse;
import Course.demo.Dto.Response.ProveUserResponse;
import Course.demo.Dto.Response.UserResponse;
import Course.demo.Entity.Course;
import Course.demo.Entity.User;
import Course.demo.Entity.UserCourse;
import Course.demo.Mapper.CourseMapper;
import Course.demo.Repository.CourseRepository;
import Course.demo.Repository.UserRepository;
import Course.demo.Util.SecurityUtil;
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
    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.userRepository = userRepository;
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
                userResponses
        );
    }


}

