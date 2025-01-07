package Course.demo.Service;

import Course.demo.Dto.Reponse.CreateUserReponse;
import Course.demo.Dto.Reponse.UpdateUserReponse;
import Course.demo.Dto.Request.UpdateUserReq;
import Course.demo.Dto.Request.UserReq;
import Course.demo.Entity.User;
import Course.demo.Mapper.UserMapper;
import Course.demo.Repository.UserRepository;
import Course.demo.Util.constant.GenderEnum;
import Course.demo.Util.error.IdInvaldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired

    private UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    public User createUser(UserReq userReq) throws ApplicationContextException {
        User user = userMapper.toUser(userReq);
        boolean checkMail = this.userRepository.existsByEmail(user.getEmail());
        boolean checkPhone = this.userRepository.existsByPhone(user.getPhone());
        if (checkMail) {
            throw new ApplicationContextException("Email đã tồn tại");
        }
        if (checkPhone) {
            throw new ApplicationContextException("Phone đã tồn tại");
        }
        System.out.println("Mapped user: " + userReq);

        return userRepository.save(user);
    }
    public User fetchById(int id) throws IdInvaldException {
        Optional<User> user = this.userRepository.findById(id);
        if (!user.isPresent()) {
            throw new IdInvaldException("User not found");
        }
        return user.get();
    }

//    public ResultPaginationDTO fetchAll(Specification<User> spec, Pageable pageable){
//        Page<User> pageUsers = this.userRepositoty.findAll(spec, pageable);
//        ResultPaginationDTO rs = new ResultPaginationDTO();
//        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
//
//        meta.setPage(pageable.getPageNumber()+ 1);
//        meta.setPageSize(pageable.getPageSize());
//        meta.setPages(pageUsers.getTotalPages());
//        meta.setTotal(pageUsers.getTotalElements());
//
//        User
//
//
//
//    }
    public User updateUser(UpdateUserReq userReqFe) throws ApplicationContextException {

        Optional<User> optionalUser = this.userRepository.findById(userReqFe.getId());
        if (!optionalUser.isPresent()) {
            throw new ApplicationContextException("User not found");
        }
        User existingUser = optionalUser.get();
        // Kiểm tra email và phone đã tồn tại nhưng không phải của user hiện tại
        if (userReqFe.getEmail() != null && !userReqFe.getEmail().isEmpty()) {
            boolean checkMail = this.userRepository.existsByEmail(userReqFe.getEmail());
            if (checkMail && !existingUser.getEmail().equals(userReqFe.getEmail())) {
                throw new ApplicationContextException("Email đã tồn tại");
            }
            existingUser.setEmail(userReqFe.getEmail());
        }
        if (userReqFe.getPhone() != null && !userReqFe.getPhone().isEmpty()) {
            boolean checkPhone = this.userRepository.existsByPhone(userReqFe.getPhone());
            if (checkPhone && !existingUser.getPhone().equals(userReqFe.getPhone())) {
                throw new ApplicationContextException("Phone đã tồn tại");
            }
            existingUser.setPhone(userReqFe.getPhone());
        }
        // Cập nhật các trường khác nếu có
        if (userReqFe.getFirstName() != null && !userReqFe.getFirstName().isEmpty()) {
            existingUser.setFirstName(userReqFe.getFirstName());
        }
        if (userReqFe.getLastName() != null && !userReqFe.getLastName().isEmpty()) {
            existingUser.setLastName(userReqFe.getLastName());
        }
        if (userReqFe.getAddress() != null && !userReqFe.getAddress().isEmpty()) {
            existingUser.setAddress(userReqFe.getAddress());
        }
        if (userReqFe.getBirthday() != null) {
            existingUser.setBirthday(userReqFe.getBirthday());
        }
        if (userReqFe.getGender() != null && !userReqFe.getGender().isEmpty()) {
            existingUser.setGender(GenderEnum.valueOf(userReqFe.getGender().toUpperCase()));
        }
        return userRepository.save(existingUser);
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
