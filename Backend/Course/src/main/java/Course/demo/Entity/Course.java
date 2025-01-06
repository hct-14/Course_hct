package Course.demo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String descriptionName;

    private String description;
    private String provide;
    private String request;
    private float rating;
    // Quan hệ Many-to-One với Category
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Quan hệ Many-to-One với User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Quan hệ One-to-Many với Comment
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
}
