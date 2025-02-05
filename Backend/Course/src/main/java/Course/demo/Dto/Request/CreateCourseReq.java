package Course.demo.Dto.Request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class CreateCourseReq {
    @NotNull(message = "Name không được để trống")
    private String name;
    @NotNull(message = "DescriptionName không được để trống")
    private String descriptionName;
    @NotNull(message = "Description không được để trống")
    private String description;
    @NotNull(message = "Provide không được để trống")
    private String provide;
    @NotNull(message = "Request không được để trống")
    private String request;

    private float rating;
}
