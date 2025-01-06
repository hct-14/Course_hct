package Course.demo.Entity;

import Course.demo.Util.constant.GenderEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
//    @NotBlank(message = "Password is required.")
//    @Size(min = 6, message = "Password must be at least 6 characters.")
    private String password;
    private String phone;
    private String address;
    private String email;
    private LocalDate  birthday;

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

    // Quan hệ One-to-Many với Course
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses;
}
