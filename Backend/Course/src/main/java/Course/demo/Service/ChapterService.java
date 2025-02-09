package Course.demo.Service;
import Course.demo.Dto.Request.CreateChapterReq;
import Course.demo.Dto.Request.UpdateChapterReq;
import Course.demo.Dto.Response.ChapterResponse;
import Course.demo.Dto.Response.CourseResponse;
import Course.demo.Dto.Response.LessonChapterResponse;
import Course.demo.Dto.Response.Page.ResultPaginationDTO;
import Course.demo.Dto.Response.ProveUserResponse;
import Course.demo.Entity.Chapter;
import Course.demo.Entity.Course;
import Course.demo.Entity.Lesson;
import Course.demo.Mapper.ChapterMapper;
import Course.demo.Repository.ChapterRepository;
import Course.demo.Repository.CourseRepository;
import Course.demo.Util.error.IdInvaldException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChapterService {

    private ChapterRepository chapterRepository;
    private CourseRepository courseRepository;
    private CourseService courseService;
    private ChapterMapper chapterMapper;
    public ChapterService(ChapterRepository chapterRepository, CourseRepository courseRepository, ChapterMapper chapterMapper, CourseService courseService) {
        this.chapterRepository = chapterRepository;
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.chapterMapper = chapterMapper;
    }


    public Chapter createChapter(CreateChapterReq chapterReq) throws IdInvaldException {
        Chapter chapter = chapterMapper.toChapter(chapterReq);

        Optional<Course> optionalCourse = this.courseRepository.findById(chapterReq.getCourseId());
        if (!optionalCourse.isPresent()) {
            throw new IdInvaldException("Course not found");
        }
        chapter.setCourse(optionalCourse.get());
        return chapterRepository.save(chapter);
    }

    public Chapter updateChapter(UpdateChapterReq chapterReq) throws IdInvaldException {
        Chapter chapter = chapterMapper.toChapterUpdate(chapterReq);
        Optional<Chapter> optionalChapter = this.chapterRepository.findById(chapter.getId());
        if (!optionalChapter.isPresent()) {
            throw new IdInvaldException("Course not found");
        }
        Chapter updatedChapter = optionalChapter.get();
        if (updatedChapter.getTitle() !=null) {
            updatedChapter.setTitle(chapter.getTitle());
        }

        return chapterRepository.save(updatedChapter);
    }
    public Chapter fetchById(int id) throws IdInvaldException {
        Optional<Chapter> optionalChapter = this.chapterRepository.findById(id);
        if (!optionalChapter.isPresent()) {
            throw new IdInvaldException("Course not found");
        }
        return optionalChapter.get();
    }

    public ResultPaginationDTO fetchAll(Specification<Chapter> spec, Pageable pageable){
        Page<Chapter> page = this.chapterRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());
        res.setMeta(meta);
        List<ChapterResponse> chapters = page.getContent().stream().map(item->this.convertToChapterResponse(item)
        ).collect(Collectors.toList());
        res.setResult(chapters);
        return res;
    }

    public void deleteChapter(int id) throws IdInvaldException {
        Optional<Chapter> optionalChapter = this.chapterRepository.findById(id);
        if (!optionalChapter.isPresent()) {
            throw new IdInvaldException("Course not found");
        }
        Chapter chapter = optionalChapter.get();
        if (chapter.getCourse()!=null) {
            chapter.setCourse(null);
        }
        this.chapterRepository.deleteById(id);
    }

    public ChapterResponse convertToChapterResponse(Chapter chapter) {
        // Khởi tạo danh sách lessons rỗng
        List<LessonChapterResponse> lessons = new ArrayList<>();

        if (chapter.getLessons() != null) {
            lessons = chapter.getLessons().stream().map(item -> new LessonChapterResponse(
                    item.getId(),
                    item.getTitle(),
                    item.getDescription(),
                    item.getImage()
            )).collect(Collectors.toList());
        }

        CourseResponse courseResponse = null;
        if (chapter.getCourse() != null) {
            courseResponse = new CourseResponse(
                    chapter.getCourse().getId(),
                    chapter.getCourse().getName(),
                    chapter.getCourse().getDescriptionName(),
                    chapter.getCourse().getDescription(),
                    chapter.getCourse().getProvide(),
                    chapter.getCourse().getRequest(),
                    chapter.getCourse().getRating(),
                    chapter.getCourse().getPrice(),
                    chapter.getCourse().getSkyHighPrices(),
                    null
            );
        }

        return new ChapterResponse(
                chapter.getId(),
                chapter.getTitle(),
                courseResponse,
                lessons
        );
    }


}
