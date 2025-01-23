package Course.demo.Dto.Request;

import Course.demo.Entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProveCreateReq {

    @NotNull(message = "country không được để trống")
    private String country;

    @NotBlank(message = "nameFacility không được để trống")

    private String nameFacility;
    @NotBlank(message = "nameFacility không được để trống")

    private String expertise;
    @NotBlank(message = "nameFacility không được để trống")

    private String city;
    @NotBlank(message = "image không được để trống")

    private String image;
    @NotBlank(message = "type không được để trống")

    private String type;

    private User user;
}
