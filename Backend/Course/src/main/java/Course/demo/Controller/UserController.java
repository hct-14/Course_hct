package Course.demo.Controller;

import Course.demo.Dto.Response.CreateUserReponse;
import Course.demo.Dto.Response.Page.ResultPaginationDTO;
import Course.demo.Dto.Response.UpdateUserReponse;
import Course.demo.Dto.Response.UserReponse;
import Course.demo.Dto.Request.UpdateUserReq;
import Course.demo.Dto.Request.UserReq;
import Course.demo.Entity.User;
import Course.demo.Service.UserService;
import Course.demo.Util.error.IdInvaldException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create")

    public ResponseEntity<CreateUserReponse> test(@Valid @RequestBody UserReq userReq) {
        String hashPassword = this.passwordEncoder.encode(userReq.getPassword());

     User userSave = this.userService.createUser(userReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.converToCreateUserReponse(userSave));
    }
    @PutMapping("update")
    public ResponseEntity<UpdateUserReponse> update(@Valid @RequestBody UpdateUserReq userReq) {

        User updatedUser = this.userService.updateUser(userReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.converToUpdateUserReponse(updatedUser));
    }
    @GetMapping("/fetch-all")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAll(spec, pageable));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable int id) throws IdInvaldException {
       this.userService.deleteById(id);
     return ResponseEntity.status(HttpStatus.OK).body("delete success");
    }

    @GetMapping("fetch-by-id/{id}")
    public ResponseEntity<UserReponse> findById(@PathVariable int id) throws IdInvaldException {
        User user = this.userService.fetchById(id);

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.converToUserReponse(user));

    }

}
