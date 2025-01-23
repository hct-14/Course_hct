package Course.demo.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProveUserResponse {

    private int id;
    private String country;
    private String nameFacility;
    private String expertise;
    private String city;
    private String image;
    private String type;

}
