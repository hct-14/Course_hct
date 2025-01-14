package Course.demo.Mapper;

import Course.demo.Dto.Request.CreateRoleReq;
import Course.demo.Dto.Request.UpdateRoleReq;
import Course.demo.Entity.Permission;
import Course.demo.Entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {
//    Role toRole(CreateRoleReq roleReq);

//    Role toRoleUpdate(UpdateRoleReq roleReq);
@Mapping(target = "permissions", expression = "java(mapPermissions(roleReq.getPermissions()))")
Role toRole(CreateRoleReq roleReq);

    Role toRoleUpdate(UpdateRoleReq roleReq);

    // Phương thức ánh xạ tùy chỉnh từ List<String> sang List<Permission>
    default List<Permission> mapPermissions(List<String> permissions) {
        if (permissions == null) {
            return null;
        }
        return permissions.stream()
                .map(name -> {
                    Permission permission = new Permission();
                    permission.setName(name);
                    return permission;
                })
                .collect(Collectors.toList());
    }
}
