package Course.demo.Controller;

import Course.demo.Dto.Request.CreateRoleReq;
import Course.demo.Entity.Role;
import Course.demo.Service.RoleService;
import Course.demo.Util.error.IdInvaldException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    public RoleController(RoleService roleService) {

        this.roleService = roleService;
    }

    @PostMapping("/create")
    public Role createRole(@Valid @RequestBody CreateRoleReq roleReq) throws IdInvaldException   {
        return this.roleService.createRole(roleReq);
    }
}
