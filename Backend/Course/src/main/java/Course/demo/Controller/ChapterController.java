package Course.demo.Controller;

import Course.demo.Dto.Request.CreateChapterReq;
import Course.demo.Entity.Chapter;
import Course.demo.Service.ChapterService;
import Course.demo.Util.error.IdInvaldException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;

@RestController
@RequestMapping("/api/chapter")
public class ChapterController {

    private final ChapterService chapterService;
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }


    @PostMapping("/create")
    public ResponseEntity<Chapter> createChapter(@RequestBody @Valid CreateChapterReq chapterReq) throws IdInvaldException {
        Chapter chapter = this.chapterService.createChapter(chapterReq);
        return ResponseEntity.status(HttpStatus.OK).body(chapter);
    }

}
