package Course.demo.Dto.Request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserReq {

    @NotBlank(message = "First name is required.")
    private String firstName;
    @NotBlank(message = "Last name is required.")
    private String lastName;
    private String gender;
    @Pattern(regexp = "\\d+", message = "Phone number must contain only digits.")
    @Size(max = 11, message = "Phone number must not exceed 15 characters.")
    private String phone;
    private String address;
    @Email(message = "Invalid email format.")
    private String email;
    @Past(message = "Birthday must be a past date.")
    private LocalDate birthday;
    private String linkFb;
    private String avt;
}
