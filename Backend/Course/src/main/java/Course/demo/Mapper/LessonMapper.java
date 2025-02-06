package Course.demo.Mapper;

import Course.demo.Dto.Request.CreateLessonReq;
import Course.demo.Dto.Request.UpdateLessonReq;
import Course.demo.Entity.Lesson;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface LessonMapper {
    Lesson toLesson(CreateLessonReq lesson);
    Lesson toLessonUpdate(UpdateLessonReq lesson);

}
