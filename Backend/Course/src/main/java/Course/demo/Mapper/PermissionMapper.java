package Course.demo.Mapper;

import Course.demo.Dto.Request.CreatePermissionReq;
import Course.demo.Dto.Request.UpdatePermissionReq;
import Course.demo.Entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface PermissionMapper {
    Permission toPermission(CreatePermissionReq createPermissionReq);
    Permission toPermissionUpdate(UpdatePermissionReq updatePermissionReq);

}
