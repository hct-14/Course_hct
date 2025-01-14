package Course.demo.Mapper;

import Course.demo.Dto.Request.CreateCourseReq;
import Course.demo.Entity.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface CourseMapper {
    Course toCourse(CreateCourseReq course);
}
