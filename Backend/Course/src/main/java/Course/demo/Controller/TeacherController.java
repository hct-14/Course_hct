package Course.demo.Controller;


import Course.demo.Dto.Reponse.TeacherReponse;
import Course.demo.Dto.Request.RegisterTeacherReq;
import Course.demo.Entity.User;
import Course.demo.Service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }


    @PostMapping("/create")
        public ResponseEntity<TeacherReponse> registerTeacher(@Valid @RequestBody RegisterTeacherReq teacherReq) {

            User user = this.teacherService.register(teacherReq);

            return ResponseEntity.status(HttpStatus.OK).body(this.teacherService.convertoTeacherDTO(user));

        }
    @PutMapping("/update")
        public ResponseEntity<TeacherReponse> updateTecher(@Valid @RequestBody RegisterTeacherReq  registerTeacherReq) {
        User user = this.teacherService.updateTeacher(registerTeacherReq);
        return ResponseEntity.status(HttpStatus.OK).body(this.teacherService.convertoTeacherDTO(user));

        }

}
