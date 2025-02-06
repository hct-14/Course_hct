package Course.demo.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLessonReq {

//    private int id;
    private String title;
    private String description;
    private int chapterId;
    private String image;

}
