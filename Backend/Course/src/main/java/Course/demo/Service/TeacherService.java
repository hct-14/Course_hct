package Course.demo.Service;

import Course.demo.Dto.Response.TeacherReponse;
import Course.demo.Dto.Request.RegisterTeacherReq;
import Course.demo.Entity.Role;
import Course.demo.Entity.User;
import Course.demo.Mapper.UserMapper;
import Course.demo.Repository.UserRepository;
import Course.demo.Util.constant.TeacherStatusEnum;
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

    public User updateTeacher(RegisterTeacherReq registerTeacherReq) throws ApplicationContextException {
        // Chuyển đổi RegisterTeacherReq thành User
        User user = userMapper.toTeacher(registerTeacherReq);

        // Tìm User đã tồn tại trong cơ sở dữ liệu
        Optional<User> optionalUser = this.userRepository.findById(registerTeacherReq.getId());

        if (!optionalUser.isPresent()) {
            throw new ApplicationContextException("User not found");
        }

        User existingUser = optionalUser.get();

        existingUser.setTeacherStatus(TeacherStatusEnum.PENDING);

        if (user.getLinkFb() != null && !user.getLinkFb().isEmpty()) {
            existingUser.setLinkFb(user.getLinkFb());
        }

        if(user.getDescription() != null && !user.getDescription().isEmpty()) {
            existingUser.setDescription(user.getDescription());
        }

        if (user.getExp() >= 0) {
            existingUser.setExp(user.getExp());
        }

        if (user.getCvUrl() != null && !user.getCvUrl().isEmpty()) {
            existingUser.setCvUrl(user.getCvUrl());
        }

        // Giữ nguyên các trường như name, birthday, phone nếu không có giá trị mới
        if (user.getName() != null && !user.getName().isEmpty()) {
            existingUser.setName(user.getName());
        }

        if (user.getBirthday() != null) {
            existingUser.setBirthday(user.getBirthday());
        }

        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            existingUser.setPhone(user.getPhone());
        }

        if (user.getEmail() != null && !user.getEmail().isEmpty()) {
            existingUser.setEmail(user.getEmail());
        }

        if (user.getAddress() != null && !user.getAddress().isEmpty()) {
            existingUser.setAddress(user.getAddress());
        }

        // Lưu đối tượng người dùng cập nhật vào cơ sở dữ liệu
        return userRepository.save(existingUser); // Lưu existingUser đã được cập nhật
    }


//    public User updateTeacher(RegisterTeacherReq registerTeacherReq) throws ApplicationContextException {
//        User user = userMapper.toTeacher(registerTeacherReq);
//        Optional<User> optionalUser = this.userRepository.findById(user.getId());
//        if (!optionalUser.isPresent()) {
//            throw new ApplicationContextException("User not found");
//        }
//        User existingUser = optionalUser.get();
//        if (user.getLinkFb() != null && !user.getLinkFb().isEmpty()) {
//            existingUser.setLinkFb(user.getLinkFb());
//
//        }
//        if (user.getDescription() != null && !user.getDescription().isEmpty()) {
//            existingUser.setDescription(user.getDescription());
//        }
//        if (user.getExp() >=0 ){
//            existingUser.setExp(user.getExp());
//        }
//        if (user.getCvUrl() != null && !user.getCvUrl().isEmpty()) {
//            existingUser.setCvUrl(user.getCvUrl());
//        }
//        Role userRole = existingUser.getRole();
//        if (userRole == null || (!"ADMIN".equals(userRole.getRoleName()) && !"SUPER_ADMIN".equals(userRole.getRoleName()))) {
//            throw new ApplicationContextException("Không có quyền thay đổi trạng thái giáo viên");
//        }
//        return userRepository.save(existingUser);
//    }

        public TeacherReponse convertoTeacherDTO(User user){
        TeacherReponse teacherReponse = new TeacherReponse();
        teacherReponse.setId(user.getId());
        teacherReponse.setLinkFb(user.getLinkFb());
        teacherReponse.setDescription(user.getDescription());
        teacherReponse.setTeacherStatus(user.getTeacherStatus());
        teacherReponse.setCvUrl(user.getCvUrl());
        teacherReponse.setExp(user.getExp());
        return teacherReponse;
        }



}
