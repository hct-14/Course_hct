package Course.demo.Controller;

import Course.demo.Dto.Request.CreateLessonReq;
import Course.demo.Dto.Request.UpdateLessonReq;
import Course.demo.Dto.Response.LessonResponse;
import Course.demo.Dto.Response.Page.ResultPaginationDTO;
import Course.demo.Entity.Lesson;
import Course.demo.Service.LessonService;
import Course.demo.Util.annotation.ApiMessage;
import Course.demo.Util.error.IdInvaldException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lesson")
public class LessonController {
    private LessonService lessonService;
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PostMapping("create")
    @ApiMessage("success")
    public ResponseEntity<LessonResponse> createLesson(@RequestBody @Valid CreateLessonReq request)throws IdInvaldException {
        Lesson lesson = lessonService.createLesson(request);
        return ResponseEntity.status(HttpStatus.OK).body(this.lessonService.convertToLessonResponse(lesson));
    }
    @PutMapping("update")
    @ApiMessage("success")

    public ResponseEntity<LessonResponse> updateLesson(@RequestBody @Valid UpdateLessonReq request)throws IdInvaldException {
        Lesson lesson = lessonService.updateLesson(request);
        return ResponseEntity.status(HttpStatus.OK).body(this.lessonService.convertToLessonResponse(lesson));
    }
    @GetMapping("fetch-by-{id}")
    @ApiMessage("success")
    public ResponseEntity<LessonResponse> fetchLessonById(@PathVariable int id)throws IdInvaldException {
        return ResponseEntity.status(HttpStatus.OK).body(this.lessonService.convertToLessonResponse(this.lessonService.fetchById(id)));
    }
    @GetMapping("fetchAll")
    @ApiMessage("success")
    public ResponseEntity<ResultPaginationDTO> fetchAllLessons(@Filter Specification<Lesson> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.lessonService.fetchAll(spec, pageable));
    }
    @DeleteMapping("delete/{id}")
    @ApiMessage("success")
    public ResponseEntity<String> deleteLesson(@PathVariable int id)throws IdInvaldException {
        this.lessonService.deleteLesson(id);
       return ResponseEntity.status(HttpStatus.OK).body("success");
    }

}
