package Course.demo.Service;

import Course.demo.Dto.Reponse.TeacherReponse;
import Course.demo.Dto.Request.RegisterTeacherReq;
import Course.demo.Entity.Role;
import Course.demo.Entity.User;
import Course.demo.Mapper.UserMapper;
import Course.demo.Repository.UserRepository;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // Constructor injection for dependencies
    public TeacherService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User register(RegisterTeacherReq registerTeacherReq) throws ApplicationContextException {
        User user = userMapper.toTeacher(registerTeacherReq);
        Optional<User> optionalUser = this.userRepository.findById(user.getId());

        if (!optionalUser.isPresent()) {
            throw new ApplicationContextException("User not found");
        }
        User existingUser = optionalUser.get();

        if (user.getLinkFb() != null && !user.getLinkFb().isEmpty()) {
            existingUser.setLinkFb(user.getLinkFb());
        }
        if(user.getDescription() != null && !user.getDescription().isEmpty()) {
            existingUser.setDescription(user.getDescription());
        }
        if (user.getExp() !=null ){
            existingUser.setExp(user.getExp());
        }
        if (user.getCvUrl() != null && !user.getCvUrl().isEmpty()) {
            existingUser.setCvUrl(user.getCvUrl());
        }

        return userRepository.save(user);
    }

    public User updateTeacher(RegisterTeacherReq registerTeacherReq) throws ApplicationContextException {
        User user = userMapper.toTeacher(registerTeacherReq);
        Optional<User> optionalUser = this.userRepository.findById(user.getId());
        if (!optionalUser.isPresent()) {
            throw new ApplicationContextException("User not found");
        }
        User existingUser = optionalUser.get();
        if (user.getLinkFb() != null && !user.getLinkFb().isEmpty()) {
            existingUser.setLinkFb(user.getLinkFb());

        }
        if (user.getDescription() != null && !user.getDescription().isEmpty()) {
            existingUser.setDescription(user.getDescription());
        }
        if (user.getExp() !=null ){
            existingUser.setExp(user.getExp());
        }
        if (user.getCvUrl() != null && !user.getCvUrl().isEmpty()) {
            existingUser.setCvUrl(user.getCvUrl());
        }
        Role userRole = existingUser.getRole();
        if (userRole == null || (!"ADMIN".equals(userRole.getRoleName()) && !"SUPER_ADMIN".equals(userRole.getRoleName()))) {
            throw new ApplicationContextException("Không có quyền thay đổi trạng thái giáo viên");
        }
        return userRepository.save(existingUser);
    }

        public TeacherReponse convertoTeacherDTO(User user){
        TeacherReponse teacherReponse = new TeacherReponse();
        teacherReponse.setId(user.getId());
        teacherReponse.setLinkFb(user.getLinkFb());
        teacherReponse.setDescription(user.getDescription());
        teacherReponse.setTeacherStatus(user.getTeacherStatus());

        return teacherReponse;
        }


}
