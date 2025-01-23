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
public class ProveUpdateReq {
    private int id;
    private String country;

    private String nameFacility;

    private String expertise;

    private String city;

    private String image;

    private String type;

    private User user;
}
