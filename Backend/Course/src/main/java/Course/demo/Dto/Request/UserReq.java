package Course.demo.Dto.Request;

import Course.demo.Validator.DobConstaint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class UserReq {
    private int id;
    @NotBlank(message = "First name is required.")
    private String name;

//    @NotBlank(message = "Last name is required.")
//    private String lastName;

    @NotBlank(message = "Password is required.")
    @Size( min = 6, message = "Password must be at least 6 characters.")
    private String password;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @Pattern(regexp = "\\d{10,15}", message = "Phone number must contain only digits and be between 10 and 15 characters.")
    private String phone;
    private String address;
    private String gender;
    @JsonFormat(pattern = "yyyy/MM/dd")
    @DobConstaint(message = "Năm sinh không thể lớn hơn năm hiện tại")
    private LocalDate  birthday;
}
