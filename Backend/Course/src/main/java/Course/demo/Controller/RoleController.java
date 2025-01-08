package Course.demo.Controller;

import Course.demo.Dto.Reponse.Page.ResultPaginationDTO;
import Course.demo.Dto.Reponse.RoleReponse;
import Course.demo.Dto.Request.CreateRoleReq;
import Course.demo.Dto.Request.UpdateRoleReq;
import Course.demo.Entity.Role;
import Course.demo.Service.RoleService;
import Course.demo.Util.annotation.ApiMessage;
import Course.demo.Util.error.IdInvaldException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    @GetMapping("/fetch-All")
    public ResponseEntity<ResultPaginationDTO> fetchAllRoles(@Filter Specification<Role> spec, Pageable pageable) throws IdInvaldException   {
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.fetchAll(spec, pageable));
    }
    @GetMapping("fetch-by-id/{id}")
    public ResponseEntity<RoleReponse> fetchRoleById(@PathVariable int id) throws IdInvaldException   {
        Role role = this.roleService.fetchById(id);
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.converToRoleReponse(role));
    }
    @DeleteMapping("/delete/{id}")
    @ApiMessage("Xoá thành công")
    public ResponseEntity<String> deleteRole(@PathVariable int id) throws IdInvaldException   {
        this.roleService.deleteRole(id);
        return ResponseEntity.status(HttpStatus.OK).body("Xoá thành công");
    }
}
