package Course.demo.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserReponse {

    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private String address;
    private String email;
    private LocalDate birthday;

}
