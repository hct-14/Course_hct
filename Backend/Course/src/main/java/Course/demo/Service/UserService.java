package Course.demo.Service;

import Course.demo.Dto.Reponse.CreateUserReponse;
import Course.demo.Dto.Reponse.UpdateUserReponse;
import Course.demo.Dto.Request.UserReq;
import Course.demo.Entity.User;
import Course.demo.Mapper.UserMapper;
import Course.demo.Repository.UserRepositoty;
import Course.demo.Util.constant.GenderEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepositoty userRepositoty;
    @Autowired

    private UserMapper userMapper;

    public UserService(UserRepositoty userRepositoty, UserMapper userMapper) {
        this.userRepositoty = userRepositoty;
        this.userMapper = userMapper;
    }
    public User createUser(UserReq userReq) throws ApplicationContextException {
        User user = userMapper.toUser(userReq);
        boolean checkMail = this.userRepositoty.existsByEmail(user.getEmail());
        boolean checkPhone = this.userRepositoty.existsByPhone(user.getPhone());
        if (checkMail) {
            throw new ApplicationContextException("Email đã tồn tại");
        }
        if (checkPhone) {
            throw new ApplicationContextException("Phone đã tồn tại");
        }
        System.out.println("Mapped user: " + userReq);

        return userRepositoty.save(user);
    }
    public User updateUser(UserReq userReqFe) throws ApplicationContextException {
        Optional<User> optionalUser = this.userRepositoty.findById(userReqFe.getId());
        if (!optionalUser.isPresent()) {
            throw new ApplicationContextException("User not found");
        }
        User existingUser = optionalUser.get();
        // Kiểm tra email đã tồn tại nhưng không phải của user hiện tại
        boolean checkMail = this.userRepositoty.existsByEmail(userReqFe.getEmail());
        if (checkMail) {
            throw new ApplicationContextException("Email đã tồn tại");
        }
        // Kiểm tra phone đã tồn tại nhưng không phải của user hiện tại
        boolean checkPhone = this.userRepositoty.existsByPhone(userReqFe.getPhone());
        if (checkPhone) {
            throw new ApplicationContextException("Phone đã tồn tại");
        }
        // Cập nhật dữ liệu nếu không null
        if (userReqFe.getFirstName() != null) {
            existingUser.setFirstName(userReqFe.getFirstName());
        }
        if (userReqFe.getLastName() != null) {
            existingUser.setLastName(userReqFe.getLastName());
        }
        if (userReqFe.getEmail() != null) {
            existingUser.setEmail(userReqFe.getEmail());
        }
        if (userReqFe.getPhone() != null) {
            existingUser.setPhone(userReqFe.getPhone());
        }
        if (userReqFe.getAddress() != null) {
            existingUser.setAddress(userReqFe.getAddress());
        }
        if (userReqFe.getBirthday() != null) {
            existingUser.setBirthday(userReqFe.getBirthday());
        }
        if (userReqFe.getGender() != null) {
            existingUser.setGender(GenderEnum.valueOf(userReqFe.getGender().toUpperCase()));
        }
        return userRepositoty.save(existingUser);
    }

    public CreateUserReponse converToCreateUserReponse(User user) {
        CreateUserReponse res = new CreateUserReponse();
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setEmail(user.getEmail());
        res.setPhone(user.getPhone());
        res.setGender(user.getGender().toString());
        res.setAddress(user.getAddress());
        res.setBirthday(user.getBirthday());
        return res;
    }
    public UpdateUserReponse converToUpdateUserReponse(User user) {
        UpdateUserReponse res = new UpdateUserReponse();
        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setEmail(user.getEmail());
        res.setPhone(user.getPhone());
        res.setGender(user.getGender().toString());
        res.setAddress(user.getAddress());
        res.setBirthday(user.getBirthday());
        res.setAvt(user.getAvt());
        return res;
    }
}
