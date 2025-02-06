package Course.demo.Controller;

import Course.demo.Dto.Request.CreateChapterReq;
import Course.demo.Dto.Request.UpdateChapterReq;
import Course.demo.Dto.Response.ChapterResponse;
import Course.demo.Dto.Response.Page.ResultPaginationDTO;
import Course.demo.Entity.Chapter;
import Course.demo.Service.ChapterService;
import Course.demo.Util.annotation.ApiMessage;
import Course.demo.Util.error.IdInvaldException;
import jakarta.validation.Valid;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;

@RestController
@RequestMapping("/api/chapter")
public class ChapterController {

    private final ChapterService chapterService;
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }


    @PostMapping("/create")
    @ApiMessage("success")

    public ResponseEntity<Chapter> createChapter(@RequestBody @Valid CreateChapterReq chapterReq) throws IdInvaldException {
        Chapter chapter = this.chapterService.createChapter(chapterReq);
        return ResponseEntity.status(HttpStatus.OK).body(chapter);
    }

    @PutMapping("update")
    @ApiMessage("success")
    public ResponseEntity<ChapterResponse> updateChapter(@RequestBody @Valid UpdateChapterReq chapterReq) throws IdInvaldException {
        Chapter chapter = this.chapterService.updateChapter(chapterReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.chapterService.convertToChapterResponse(chapter));
    }

    @GetMapping("fetch-by-{id}")
    @ApiMessage("success")

    public ResponseEntity<ChapterResponse> getChapter(@PathVariable int id) throws IdInvaldException {
        Chapter chapter = this.chapterService.fetchById(id);
        return ResponseEntity.status(HttpStatus.OK).body(this.chapterService.convertToChapterResponse(chapter));
    }

    @GetMapping("fetchAll")
    @ApiMessage("success")
    public ResponseEntity<ResultPaginationDTO> fetchAllChapters(Specification<Chapter> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.chapterService.fetchAll(spec, pageable));
    }
    @DeleteMapping("delete/{id}")
    @ApiMessage("success")

    public ResponseEntity<String> deleteChapter(@PathVariable int id) throws IdInvaldException {
        this.chapterService.deleteChapter(id);
        return ResponseEntity.status(HttpStatus.OK).body("success");
    }

}
