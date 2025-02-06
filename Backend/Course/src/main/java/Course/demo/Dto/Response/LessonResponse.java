package Course.demo.Dto.Response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponse {

    private int id;
    private String title;
    private String description;
    private int chapterId;
    private String image;
    private ChapterResponse chapter;
    private CourseResponse course;

}
