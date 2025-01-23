package Course.demo.Dto.Response;

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

    private UserProve user;

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserProve{
        private int id;
        private String name;
        private String email;
        private String phone;

    }

}
