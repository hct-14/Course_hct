package Course.demo.Dto.Reponse;

import Course.demo.Entity.Prove;
import Course.demo.Entity.Role;
import Course.demo.Entity.UserCourse;
import Course.demo.Util.constant.ExpEnum;
import Course.demo.Util.constant.GenderEnum;
import Course.demo.Util.constant.TeacherStatusEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserReponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String password;
    private String phone;
    private String address;
    private String email;
    private LocalDate birthday;
    private ExpEnum exp;
    private String cvUrl;
    private TeacherStatusEnum teacherStatus;
    private String description;
    private String refreshToken;
    private String linkFb;
    private String avt;
    private float income;
    @JsonIgnore
    private Role role;
    private List<ProveResponse> proves;
    private List<UserCourse> userCourses;
}
