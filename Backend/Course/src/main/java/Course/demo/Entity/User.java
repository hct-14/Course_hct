package Course.demo.Entity;

import Course.demo.Util.constant.ExpEnum;
import Course.demo.Util.constant.GenderEnum;
import Course.demo.Util.constant.TeacherStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;


    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    private String password;
    private String phone;
    private String address;
    private String email;
    private LocalDate birthday;
    private ExpEnum exp;
    private String cvUrl;
//    private UserStatusEnum userStatus;
    private TeacherStatusEnum teacherStatus;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String description;

    @Column(columnDefinition = "MEDIUMTEXT")
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
