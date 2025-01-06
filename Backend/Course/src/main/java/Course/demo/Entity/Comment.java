package Course.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String comment;

    // Quan hệ Many-to-One với Course
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    // Quan hệ Many-to-One với User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
