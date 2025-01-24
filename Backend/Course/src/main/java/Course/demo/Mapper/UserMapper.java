package Course.demo.Mapper;

import Course.demo.Dto.Request.RegisterTeacherReq;
import Course.demo.Dto.Request.UserReq;
import Course.demo.Entity.User;
import Course.demo.Util.constant.TeacherStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserReq userReq);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "teacherStatus", target = "teacherStatus")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "linkFb", target = "linkFb")
    @Mapping(source = "exp", target = "exp")
    @Mapping(source = "cvUrl", target = "cvUrl")
    User toTeacher(RegisterTeacherReq registerTeacherReq);

}

