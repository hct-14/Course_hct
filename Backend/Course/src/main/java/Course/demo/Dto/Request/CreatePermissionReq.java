package Course.demo.Dto.Request;

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
public class CreatePermissionReq {


    @NotBlank(message = "Name cannot be blank or empty")
    private String name;

    @NotBlank(message = "Description cannot be blank or empty")
    private String description;
}
