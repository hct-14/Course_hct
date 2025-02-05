package Course.demo.Dto.Request;

import Course.demo.Entity.Lesson;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateChapterReq {
    private int id;
    @NotNull(message = "Title không được để trống")
    private String title;
//    private String images;
    private int courseId;
    private List<Lesson> lessons;
}
