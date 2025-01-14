package Course.demo.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserReponse {

    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private String address;
    private String email;
    private LocalDate birthday;
    private String avt;
}
