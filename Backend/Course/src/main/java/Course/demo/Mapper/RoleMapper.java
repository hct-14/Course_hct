package Course.demo.Mapper;

import Course.demo.Dto.Request.CreateRoleReq;
import Course.demo.Entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toRole(CreateRoleReq roleReq);
}
