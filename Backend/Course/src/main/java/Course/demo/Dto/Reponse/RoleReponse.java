package Course.demo.Dto.Reponse;

import Course.demo.Entity.Permission;
import jakarta.validation.constraints.NotNull;
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
