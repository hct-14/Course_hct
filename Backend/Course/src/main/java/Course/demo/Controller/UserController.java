package Course.demo.Controller;

import Course.demo.Dto.Reponse.CreateUserReponse;
import Course.demo.Dto.Reponse.Page.ResultPaginationDTO;
import Course.demo.Dto.Reponse.UpdateUserReponse;
import Course.demo.Dto.Request.UpdateUserReq;
import Course.demo.Dto.Request.UserReq;
import Course.demo.Entity.User;
import Course.demo.Service.UserService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import java.util.List;


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
    @GetMapping("/fetch-all")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAll(spec, pageable));
    }

}
