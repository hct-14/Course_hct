package Course.demo.Dto.Response;

import Course.demo.Util.constant.TeacherStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherReponse {

    private int id;
    private String description;
    private String linkFb;
    private int exp;
    private String cvUrl;
    private TeacherStatusEnum teacherStatus;

}
