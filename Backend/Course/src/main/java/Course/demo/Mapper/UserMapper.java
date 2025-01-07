package Course.demo.Mapper;

import Course.demo.Dto.Request.RegisterTeacherReq;
import Course.demo.Dto.Request.UserReq;
import Course.demo.Entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserReq userReq);
    User toTeacher(RegisterTeacherReq registerTeacherReq);

}

