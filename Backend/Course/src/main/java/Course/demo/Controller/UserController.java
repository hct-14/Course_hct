package Course.demo.Controller;

import Course.demo.Dto.Reponse.CreateUserReponse;
import Course.demo.Dto.Reponse.UpdateUserReponse;
import Course.demo.Dto.Request.UpdateUserReq;
import Course.demo.Dto.Request.UserReq;
import Course.demo.Entity.User;
import Course.demo.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateUserReponse> test(@Valid @RequestBody UserReq userReq) {

    User userSave = this.userService.createUser(userReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.converToCreateUserReponse(userSave));
    }
    @PutMapping("update")
    public ResponseEntity<UpdateUserReponse> update(@Valid @RequestBody UpdateUserReq userReq) {

        User updatedUser = this.userService.updateUser(userReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.converToUpdateUserReponse(updatedUser));
    }

}
