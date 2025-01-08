package Course.demo.Dto.Request;

import Course.demo.Dto.Reponse.PermissionReponse;
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
public class UpdateRoleReq {
    @NotNull(message = "Role không được để trống")
    private int id;
    private String roleName;
    private List<String> permissions;

}

