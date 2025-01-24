package Course.demo.Dto.Request;

import Course.demo.Util.constant.TeacherStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterTeacherReq {
    private int id;
    private TeacherStatusEnum teacherStatus;
    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Facebook link cannot be empty")
    private String linkFb;

    @NotNull(message = "Experience cannot be null")
    private int exp;

    @NotBlank(message = "Facebook link cannot be empty")
    private String cvUrl;

}
