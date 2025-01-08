package Course.demo.Service;

import Course.demo.Dto.Reponse.Page.ResultPaginationDTO;
import Course.demo.Dto.Reponse.PermissionReponse;
import Course.demo.Dto.Reponse.ProveResponse;
import Course.demo.Dto.Reponse.RoleReponse;
import Course.demo.Dto.Request.CreateRoleReq;
import Course.demo.Dto.Request.UpdateRoleReq;
import Course.demo.Entity.Permission;
import Course.demo.Entity.Role;
import Course.demo.Mapper.RoleMapper;
import Course.demo.Repository.PermissionRepository;
import Course.demo.Repository.RoleRepository;
import Course.demo.Util.error.IdInvaldException;
import jakarta.persistence.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Role role = roleMapper.toRole(roleReq);
        boolean roleCheck = this.roleRepository.existsByRoleName(role.getRoleName());
        if (roleCheck) {
            throw new IdInvaldException("Role đã tồn tại!");
        }

        List<Permission> permissions = permissionRepository.findByNameIn(role.getPermissions());

        if (permissions.size() != roleReq.getPermissions().size()) {
            throw new IdInvaldException("Một số quyền không tồn tại trong hệ thống!");
        }

        role.setRoleName(roleReq.getRoleName());
        role.setPermissions(permissions);

        return this.roleRepository.save(role);
    }
    public Role updateRole(UpdateRoleReq roleReq) throws IdInvaldException {
        Role roleMap = roleMapper.toRoleUpdate(roleReq);
        Optional<Role> roleOptional = this.roleRepository.findById(roleMap.getId());
        if (roleOptional.isEmpty()) {
            throw new IdInvaldException("Role không tồn tại!");
        }
        Role role = roleOptional.get();
        if (roleMap.getRoleName() != null && !roleMap.getRoleName().equals(role.getRoleName())) {
            boolean roleCheck = this.roleRepository.existsByRoleName(roleMap.getRoleName());
            if (roleCheck) {
                throw new IdInvaldException("Role đã tồn tại!");
            }
            role.setRoleName(roleMap.getRoleName());
        }
        if (roleReq.getPermissions() != null) {
            List<Permission> currentPermissions = role.getPermissions();
            List<Permission> newPermissions = permissionRepository.findByNameIn(roleMap.getPermissions());
            if (newPermissions.size() != roleMap.getPermissions().size()) {
                throw new IdInvaldException("Một số quyền không tồn tại trong hệ thống!");
            }
            for (Permission newPermission : newPermissions) {

                if (currentPermissions.contains(newPermission)) {
                    throw new IdInvaldException("Quyền '" + newPermission.getName() + "' đã tồn tại trong Role!");
                }
                currentPermissions.add(newPermission);
            }
            role.setPermissions(currentPermissions);
        }
        return this.roleRepository.save(role);
    }

    public Role fetchById(int id)throws IdInvaldException {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (!roleOptional.isPresent()) {
            throw new IdInvaldException("Role này không tồn tại");
        }
        return roleOptional.get();
    }

    public ResultPaginationDTO fetchAll(Specification<Role> spec, Pageable pageable) {
        Page<Role> pages = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pages.getTotalPages());
        meta.setTotal(pages.getTotalElements());

        List<RoleReponse> roles = pages.stream().map(
                item->this.converToRoleReponse(item))
                .collect(Collectors.toList());

        res.setMeta(meta);
        res.setResult(roles);
        return res;

    }
    public void deleteRole(int id) throws IdInvaldException {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (!roleOptional.isPresent()) {
            throw new IdInvaldException("Role này không tồn tại");
        }
        this.roleRepository.delete(roleOptional.get());
    }


    public RoleReponse converToRoleReponse(Role role) {
        RoleReponse roleReponse = new RoleReponse();
        roleReponse.setId(role.getId());
        roleReponse.setRoleName(role.getRoleName());
        List<PermissionReponse> permissions = role.getPermissions().stream().map(permission -> new PermissionReponse(
                permission.getId(),
                permission.getName(),
                permission.getDescription()
        )).collect(Collectors.toList());
        return new RoleReponse(
                role.getId(),
                role.getRoleName(),
                permissions
        );

    }

}
