package Course.demo.Dto.Reponse;

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
    private String exp;
    private String teacher;
    private String cvUrl;
    private TeacherStatusEnum teacherStatus;

}
