package Course.demo.Controller;

import Course.demo.Dto.Request.CreateCourseReq;
import Course.demo.Dto.Request.UpdateCourseReq;
import Course.demo.Dto.Response.CourseResponse;
import Course.demo.Dto.Response.Page.ResultPaginationDTO;
import Course.demo.Entity.Course;
import Course.demo.Service.CourseService;
import Course.demo.Util.annotation.ApiMessage;
import Course.demo.Util.error.IdInvaldException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;

@RestController
@RequestMapping("/api/course")
public class CourseController {
    private final CourseService courseService;
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("create")
    @ApiMessage("success")

    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CreateCourseReq course) {

        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.convertToCourseResponse(this.courseService.createCourse(course)));
    }

    @PutMapping("update")
    @ApiMessage("success")

    public ResponseEntity<CourseResponse> update(@Valid @RequestBody UpdateCourseReq courseReq)throws IdInvaldException {
        Course course = this.courseService.updateCourse(courseReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.convertToCourseResponse(course));
    }

    @GetMapping("fetch-by-{id}")
    @ApiMessage("success")
    public ResponseEntity<CourseResponse> fetchById(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.convertToCourseResponse(this.courseService.fetchById(id)));
    }
    @GetMapping("fetchAll")
    @ApiMessage("success")
    public ResponseEntity<ResultPaginationDTO> fetchAll(Specification<Course> spec, Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(this.courseService.fetchAllCourses(spec, pageable));
    }
    @DeleteMapping("delete/{id}")
    @ApiMessage("success")

    public ResponseEntity<String> delete(@PathVariable int id) {
        this.courseService.deleteCourse(id);
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

}
