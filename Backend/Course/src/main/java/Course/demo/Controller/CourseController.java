package Course.demo.Controller;

import Course.demo.Dto.Request.CreateCourseReq;
import Course.demo.Dto.Response.CourseResponse;
import Course.demo.Entity.Course;
import Course.demo.Service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    private final CourseService courseService;
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("test")
    public ResponseEntity<CourseResponse> test(@Valid @RequestBody CreateCourseReq course) {

        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.convertToCourseResponse(this.courseService.createCourse(course)));
    }

}
