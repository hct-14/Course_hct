package Course.demo.Service;

import Course.demo.Dto.Request.CreateRoleReq;
import Course.demo.Entity.Permission;
import Course.demo.Entity.Role;
import Course.demo.Mapper.RoleMapper;
import Course.demo.Repository.PermissionRepository;
import Course.demo.Repository.RoleRepository;
import Course.demo.Util.error.IdInvaldException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.roleMapper = roleMapper;
    }

    public Role createRole(CreateRoleReq roleReq) throws IdInvaldException {
        // Kiểm tra xem role đã tồn tại chưa
        boolean roleCheck = this.roleRepository.existsByRoleName(roleReq.getRoleName());
        if (roleCheck) {
            throw new IdInvaldException("Role đã tồn tại!");
        }

        // Kiểm tra xem từng permission có tồn tại không
        List<Permission> permissions = permissionRepository.findByNameIn(roleReq.getPermissions());

        if (permissions.size() != roleReq.getPermissions().size()) {
            throw new IdInvaldException("Một số quyền không tồn tại trong hệ thống!");
        }

        // Tạo và lưu Role với các quyền đã kiểm tra
        Role role = new Role();
        role.setRoleName(roleReq.getRoleName());
        role.setPermissions(permissions);

        return this.roleRepository.save(role);
    }
}
