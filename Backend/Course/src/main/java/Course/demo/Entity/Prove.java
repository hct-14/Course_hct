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
@Table(name = "prove")
public class Prove {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String country;
    private String nameFacility;
    private String expertise;
    private String city;
    private String image;
    private String type;

    // Quan hệ Many-to-One: Một Prove thuộc về một User
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
