package Course.demo.Dto.Reponse;

import Course.demo.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProveResponse {

    private int id;
    private String country;
    private String nameFacility;
    private String expertise;
    private String city;
    private String image;
    private String type;

}
