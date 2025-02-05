package Course.demo.Dto.Response;

import Course.demo.Entity.Course;
import Course.demo.Entity.Lesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChapterResponse {

    private int id;
    private String title;
    private CourseResponse course;
    private List<LessonChapterResponse> lessons;


}
