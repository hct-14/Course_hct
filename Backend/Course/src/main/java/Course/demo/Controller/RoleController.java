package Course.demo.Controller;

import Course.demo.Dto.Reponse.RoleReponse;
import Course.demo.Dto.Request.CreateRoleReq;
import Course.demo.Dto.Request.UpdateRoleReq;
import Course.demo.Entity.Role;
import Course.demo.Service.RoleService;
import Course.demo.Util.error.IdInvaldException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    public RoleController(RoleService roleService) {

        this.roleService = roleService;
    }

    @PostMapping("/create")
    public ResponseEntity<RoleReponse> createRole(@Valid @RequestBody CreateRoleReq roleReq) throws IdInvaldException   {
        Role role = roleService.createRole(roleReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.converToRoleReponse(role));
    }
    @PutMapping("/update")
    public ResponseEntity<RoleReponse> updateRole(@Valid @RequestBody UpdateRoleReq roleReq) throws IdInvaldException   {
        Role role = roleService.updateRole(roleReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.converToRoleReponse(role));
    }
}
