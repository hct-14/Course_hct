package Course.demo.Dto.Response;

import Course.demo.Entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RoleReponse {

    private int id;
    private String roleName;
    private List<PermissionReponse> permissions;
}
