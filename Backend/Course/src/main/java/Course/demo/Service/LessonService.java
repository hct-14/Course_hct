package Course.demo.Service;

import Course.demo.Dto.Request.CreateLessonReq;
import Course.demo.Dto.Request.UpdateLessonReq;
import Course.demo.Dto.Response.ChapterResponse;
import Course.demo.Dto.Response.CourseResponse;
import Course.demo.Dto.Response.LessonResponse;
import Course.demo.Dto.Response.Page.ResultPaginationDTO;
import Course.demo.Entity.Chapter;
import Course.demo.Entity.Course;
import Course.demo.Entity.Lesson;
import Course.demo.Mapper.LessonMapper;
import Course.demo.Repository.ChapterRepository;
import Course.demo.Repository.LessonReposotory;
import Course.demo.Util.error.IdInvaldException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class LessonService {

    private LessonReposotory lessonReposotory;
    private LessonMapper lessonMapper;
    private ChapterRepository chapterRepository;
    public LessonService(LessonReposotory lessonReposotory, LessonMapper lessonMapper, ChapterRepository chapterRepository) {
        this.lessonReposotory = lessonReposotory;
        this.lessonMapper = lessonMapper;
        this.chapterRepository = chapterRepository;
    }

    public Lesson createLesson(CreateLessonReq lessonReq) throws IdInvaldException {
        Lesson lesson = this.lessonMapper.toLesson(lessonReq);
        Optional<Chapter> chapter = chapterRepository.findById(lessonReq.getChapterId());
        if (!chapter.isPresent()) {
            throw new IdInvaldException("Chapter not found");
        }
        return this.lessonReposotory.save(lesson);
    }

    public Lesson updateLesson(UpdateLessonReq lessonReq) throws IdInvaldException {
        Lesson lesson = this.lessonMapper.toLessonUpdate(lessonReq);
        Optional<Chapter> chapter = chapterRepository.findById(lesson.getId());
        if (!chapter.isPresent()) {
            throw new IdInvaldException("Lesson not found");
        }
        if (lesson.getTitle() != null) {
            lesson.setTitle(lessonReq.getTitle());
        }
        if (lesson.getDescription() != null) {
            lesson.setDescription(lessonReq.getDescription());
        }
        if (lesson.getImage() != null) {
            lesson.setImage(lessonReq.getImage());
        }

        return this.lessonReposotory.save(lesson);

    }
    public Lesson fetchById(int id) throws IdInvaldException {
        Optional<Lesson> lesson = this.lessonReposotory.findById(id);
        if (!lesson.isPresent()) {
            throw new IdInvaldException("Lesson not found");
        }
        return lesson.get();
    }
    public ResultPaginationDTO fetchAll (Specification<Lesson> spec, Pageable pageable){
        Page<Lesson> lessons = lessonReposotory.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage( pageable.getPageNumber() + 1);
        meta.setPageSize( pageable.getPageSize());
        meta.setPages( lessons.getTotalPages());
        meta.setTotal(lessons.getTotalElements());
        res.setMeta(meta);

        List<LessonResponse> lessonList = lessons.getContent().stream().map(item-> this.convertToLessonResponse(item)
        ).collect(Collectors.toList());

        res.setResult(lessonList);
        return res;

    }
    public void deleteLesson(int id) throws IdInvaldException {
        Optional<Lesson> lesson = this.lessonReposotory.findById(id);
        if (!lesson.isPresent()) {
            throw new IdInvaldException("Lesson not found");
        }
        if (lesson.get().getChapter() != null) {
            lesson.get().setChapter(null);
        }
        this.lessonReposotory.delete(lesson.get());
    }

    public LessonResponse convertToLessonResponse(Lesson lesson) {
        if (lesson == null) {
            return null;
        }

        // Lấy thông tin Chapter
        ChapterResponse chapterResponse = null;
        if (lesson.getChapter() != null) {
            Chapter chapter = lesson.getChapter();
            Course course = chapter.getCourse();

            // Chuyển đổi Course thành CourseResponse (nếu có)
            CourseResponse courseResponse = null;
            if (course != null) {
                courseResponse = new CourseResponse(
                        course.getId(),
                        course.getName(),
                        course.getDescriptionName(),
                        course.getDescription(),
                        course.getProvide(),
                        course.getRequest(),
                        course.getRating(),
                        null
                );
            }

            // Chuyển đổi Chapter thành ChapterResponse
            chapterResponse = new ChapterResponse(
                    chapter.getId(),
                    chapter.getTitle(),
                    courseResponse,
                    null
            );
        }

        return new LessonResponse(
                lesson.getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getChapter() != null ? lesson.getChapter().getId() : 0,
                lesson.getImage(),
                chapterResponse,
                chapterResponse != null ? chapterResponse.getCourse() : null
        );
    }


}
