package Course.demo.Service;


import Course.demo.Dto.Request.CreateCourseReq;
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
import java.util.Optional;

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

        // Tạo đối tượng UserCourse và liên kết người dùng với khóa học
        UserCourse userCourse = new UserCourse();
        userCourse.setUser(currentUser);
        userCourse.setCourse(course);

        // Thêm UserCourse vào danh sách UserCourses của khóa học
        course.getUserCourses().add(userCourse);
        // Lưu khóa học và trả về kết quả
        return courseRepository.save(course);
    }



}
