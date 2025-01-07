package Course.demo.Service;

import Course.demo.Dto.Reponse.Page.ResultPaginationDTO;
import Course.demo.Dto.Reponse.PermissionReponse;
import Course.demo.Dto.Request.CreatePermissionReq;
import Course.demo.Dto.Request.UpdatePermissionReq;
import Course.demo.Entity.Permission;
import Course.demo.Entity.Role;
import Course.demo.Mapper.PermissionMapper;
import Course.demo.Repository.PermissionRepository;
import Course.demo.Repository.RoleRepository;
import Course.demo.Util.error.IdInvaldException;
import org.springframework.context.ApplicationContextException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    private PermissionRepository permissionRepository;
    private PermissionMapper permissionMapper;
    private RoleRepository roleRepositoty;

    public PermissionService(PermissionRepository permissionRepository, PermissionMapper permissionMapper, RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepositoty = roleRepository;
        this.permissionMapper = permissionMapper;

    }


    public Permission createPermission(CreatePermissionReq permissionReq) throws IdInvaldException {
        Permission permission = permissionMapper.toPermission(permissionReq);
        boolean perCheck = this.permissionRepository.existsByName(permission.getName());
        if (perCheck) {
            throw new IdInvaldException("Permission này đã tồn tại!");
        }
        return permissionRepository.save(permission);
    }

    public Permission updatePermission(UpdatePermissionReq updatePermissionReq) throws IdInvaldException {
        Permission permission = permissionMapper.toPermissionUpdate(updatePermissionReq);
        Optional<Permission> permissionOptional = this.permissionRepository.findById(updatePermissionReq.getId());
        if (!permissionOptional.isPresent()) {
            throw new ApplicationContextException("Permission does not exist");
        }
        boolean perCheck = this.permissionRepository.existsByName(permission.getName());
        if (perCheck) {
            throw new IdInvaldException("Permission này đã tồn tại!");
        }
        Permission permissionToUpdate = permissionOptional.get();
        if(!permissionToUpdate.getName().equals(permission.getName())) {
            permissionToUpdate.setName(permission.getName());
        }
        if(!permissionToUpdate.getDescription().equals(permission.getDescription())) {
            permissionToUpdate.setDescription(permission.getDescription());
        }
        return permissionRepository.save(permission);

    }

    public Permission fetch(int id){
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        if (!permissionOptional.isPresent()) {
            throw new ApplicationContextException("Permission does not exist");
        }
        return permissionOptional.get();
    }

    public void deletePermission(int id) {
            // Kiểm tra permission có tồn tại không
            Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
            if (!permissionOptional.isPresent()) {
                throw new ApplicationContextException("Permission does not exist");
            }

            Permission permission = permissionOptional.get();
            if (permission.getRoles() != null) {
                for (Role role : permission.getRoles()) {
                    role.getPermissions().remove(permission);
                    roleRepositoty.save(role);
                }
            }
            permissionRepository.delete(permission);
        }

        public Permission fetchById(int id) throws IdInvaldException {
        Optional<Permission> permissionOptional = this.permissionRepository.findById(id);
        if (!permissionOptional.isPresent()) {
            throw new IdInvaldException("Permission does not exist");
        }
        return permissionOptional.get();
        }

    public ResultPaginationDTO fetchAll(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> permissions = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber()+1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(permissions.getTotalPages());
        meta.setTotal(permissions.getTotalElements());
        resultPaginationDTO.setMeta(meta);

        List<PermissionReponse> list = permissions.getContent().stream()
                .map(item -> new PermissionReponse(
                        item.getId(),
                        item.getName(),
                        item.getDescription()
                ))
                .toList();

        // Gán danh sách vào ResultPaginationDTO
        resultPaginationDTO.setResult(list);

        return resultPaginationDTO;


    }

        public PermissionReponse converToPermission(Permission permission) {
        PermissionReponse permissionReponse = new PermissionReponse();
        permissionReponse.setId(permission.getId());
        permissionReponse.setName(permission.getName());
        permissionReponse.setDescription(permission.getDescription());
        return permissionReponse;
        }
}
