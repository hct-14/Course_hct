package Course.demo.Controller;

import Course.demo.Seeder.RoleSeeder;
import Course.demo.Seeder.UserSeeder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seeder")
public class SeederController {

    private final RoleSeeder roleSeeder;
    private final UserSeeder userSeeder;

    public SeederController(RoleSeeder roleSeeder, UserSeeder userSeeder) {
        this.roleSeeder = roleSeeder;
        this.userSeeder = userSeeder;
    }

    @GetMapping("/create")
    public ResponseEntity<String> seedAllData() {
        roleSeeder.seedData();

        userSeeder.seedDatabase();

        return ResponseEntity.ok("successfully!");
    }
}
