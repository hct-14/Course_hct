package Course.demo.Dto.Reponse;

import Course.demo.Entity.Prove;
import Course.demo.Entity.Role;
import Course.demo.Entity.UserCourse;
import Course.demo.Util.constant.ExpEnum;
import Course.demo.Util.constant.GenderEnum;
import Course.demo.Util.constant.TeacherStatusEnum;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

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

    // Quan hệ Many-to-One với Role
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    // Quan hệ One-to-Many với Prove
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prove> proves;

    // Quan hệ One-to-Many với UserCourse
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCourse> userCourses;
}
