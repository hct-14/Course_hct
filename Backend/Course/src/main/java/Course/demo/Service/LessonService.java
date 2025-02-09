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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class LessonService {
    @Value("${hct.upload-file.base-uri}")
    private String baseURI;


    private LessonReposotory lessonReposotory;
    private LessonMapper lessonMapper;
    private ChapterRepository chapterRepository;
    private FileService fileService;
    public LessonService(LessonReposotory lessonReposotory, LessonMapper lessonMapper, ChapterRepository chapterRepository, FileService fileService) {
        this.lessonReposotory = lessonReposotory;
        this.lessonMapper = lessonMapper;
        this.chapterRepository = chapterRepository;
        this.fileService = fileService;
    }

    public Lesson createLesson(CreateLessonReq lessonReq, MultipartFile file, String folder) throws IdInvaldException, URISyntaxException, IOException {
        // Tạo đối tượng Lesson mới
        Lesson lesson = new Lesson();
        lesson.setTitle(lessonReq.getTitle());
        lesson.setDescription(lessonReq.getDescription());

        // Kiểm tra và lấy Chapter từ database
        Optional<Chapter> chapter = chapterRepository.findById(lessonReq.getChapterId());
        if (!chapter.isPresent()) {
            throw new IdInvaldException("Chapter not found");
        }
        lesson.setChapter(chapter.get());

        // Kiểm tra và lưu file
        if (file != null && !file.isEmpty()) {
            // Sử dụng fileService để xử lý file
            String fileName = fileService.store(file, folder);
            lesson.setImage(fileName);  // Lưu đường dẫn vào database
        }

        // Lưu lesson vào database
        return lessonReposotory.save(lesson);
    }

    public Lesson updateLesson(UpdateLessonReq lessonReq, MultipartFile file, String folder) throws IdInvaldException, URISyntaxException, IOException {
        // Kiểm tra xem lesson có tồn tại không
        Optional<Lesson> existingLesson = lessonReposotory.findById(lessonReq.getId());
        if (!existingLesson.isPresent()) {
            throw new IdInvaldException("Lesson not found");
        }
        Lesson lesson = existingLesson.get();

        // Cập nhật các trường nếu có giá trị mới
        if (lessonReq.getTitle() != null) {
            lesson.setTitle(lessonReq.getTitle());
        }
        if (lessonReq.getDescription() != null) {
            lesson.setDescription(lessonReq.getDescription());
        }

        // Nếu có file mới được upload, lưu file mới
        if (file != null && !file.isEmpty()) {
            // Sử dụng fileService để xử lý file
            String fileName = fileService.store(file, folder);
            lesson.setImage(fileName);  // Cập nhật đường dẫn của file vào lesson
        }

        // Lưu lại lesson đã cập nhật
        return lessonReposotory.save(lesson);
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
                        course.getPrice(),
                        course.getSkyHighPrices(),

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
