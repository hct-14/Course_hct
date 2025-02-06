package Course.demo.Dto.Request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLessonReq {

//    private int id;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private int chapterId;
    @NotNull
    private String image;

}
