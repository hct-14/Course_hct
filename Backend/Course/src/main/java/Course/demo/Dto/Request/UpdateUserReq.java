package Course.demo.Dto.Request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserReq {
    private int id;
    private String firstName;
    private String lastName;
    private String gender;

    @Pattern(regexp = "\\d+", message = "Phone number must contain only digits.")
    @Size(min = 1, max = 15, message = "Phone number must be between 10 and 15 characters long.")
    private String phone;

    private String address;
    @Email(message = "Invalid email format.")
    private String email;

    @Past(message = "Birthday must be a past date.")
    private LocalDate birthday;

    private String linkFb;
    private String avt;
}
