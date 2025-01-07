package Course.demo.Dto.Request;

import Course.demo.Util.constant.ExpEnum;
import Course.demo.Util.constant.TeacherStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class RegisterTeacherReq {
    private int id;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Facebook link cannot be empty")
    private String linkFb;

    @NotNull(message = "Experience cannot be null")
    private ExpEnum exp;

    @NotBlank(message = "Facebook link cannot be empty")
    private String cvUrl;

    private TeacherStatusEnum teacherStatus;

}
