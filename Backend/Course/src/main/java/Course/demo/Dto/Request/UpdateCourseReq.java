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

public class UpdateCourseReq {
    private String name;
    private String descriptionName;
    private String description;
    private String provide;
    private String request;
    private float rating;
}
