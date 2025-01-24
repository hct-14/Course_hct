package Course.demo.Dto.Response;

import Course.demo.Entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {

    private int id;
    private String name;
    private String descriptionName;
    private String description;
    private String provide;
    private String request;
    private float rating;

    @JsonIgnore
    private List<UserResponse> users;
}
