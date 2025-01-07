package Course.demo.Controller;

import Course.demo.Dto.Reponse.Page.ResultPaginationDTO;
import Course.demo.Dto.Reponse.PermissionReponse;
import Course.demo.Dto.Request.CreatePermissionReq;
import Course.demo.Dto.Request.UpdatePermissionReq;
import Course.demo.Entity.Permission;
import Course.demo.Service.PermissionService;
import Course.demo.Util.error.IdInvaldException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/permisssion")
public class PermissionController {

    private final PermissionService permissionService;
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/create")
    public ResponseEntity<PermissionReponse> createPermission(@Valid @RequestBody CreatePermissionReq permissionReq)throws IdInvaldException {
        Permission permission = permissionService.createPermission(permissionReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.converToPermission(permission));
    }

    @PutMapping("/update")
    public ResponseEntity<PermissionReponse> updatePermission(@Valid @RequestBody UpdatePermissionReq permissionReq)throws IdInvaldException {
        Permission permission = permissionService.updatePermission(permissionReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.converToPermission(permission));
    }
    @GetMapping("/fetch-all")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<Permission> spec, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.fetchAll(spec, pageable));
    }

    @GetMapping("/fetch-by-{id}")
    public ResponseEntity<PermissionReponse> fetchById(@PathVariable int id) throws IdInvaldException {
        Permission permission = permissionService.fetchById(id);
        return ResponseEntity.status(HttpStatus.OK).body(this.permissionService.converToPermission(permission));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePermission(@PathVariable int id) throws IdInvaldException {
        permissionService.deletePermission(id);
        return ResponseEntity.status(HttpStatus.OK).body("Permission deleted");
    }

}
