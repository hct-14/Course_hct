package Course.demo.Seeder;

import Course.demo.Entity.Role;
import Course.demo.Entity.User;
import Course.demo.Repository.RoleRepository;
import Course.demo.Repository.UserRepository;
import Course.demo.Util.constant.GenderEnum;
import Course.demo.Util.constant.TeacherStatusEnum;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserSeeder {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    public UserSeeder(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public void seedDatabase() {
        // Tạo và lưu các Role vào cơ sở dữ liệu
        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        Role teacherRole = new Role();
        teacherRole.setRoleName("Teacher");
        Role studentRole = new Role();
        studentRole.setRoleName("Student");

        roleRepository.saveAll(List.of(adminRole, teacherRole, studentRole));

        // Lấy các Role từ cơ sở dữ liệu
        List<Role> roles = roleRepository.findAll();

        // Tạo dữ liệu User fake
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName(faker.name().firstName());
//            user.setLastName(faker.name().lastName());
            user.setGender(randomGender());
            user.setPassword(faker.internet().password());
            user.setPhone(faker.phoneNumber().cellPhone());
            user.setAddress(faker.address().fullAddress());
            user.setEmail(faker.internet().emailAddress());
            user.setBirthday(randomBirthday());
            user.setCvUrl(faker.internet().url());
            user.setTeacherStatus(randomTeacherStatus());
            user.setDescription(faker.lorem().sentence());
            user.setRefreshToken(faker.internet().uuid());
            user.setLinkFb(faker.internet().url());
            user.setAvt(faker.internet().avatar());
            user.setIncome(random.nextFloat() * 10000);

            // Gán role ngẫu nhiên từ danh sách roles đã lấy
            user.setRole(roles.get(random.nextInt(roles.size())));

            users.add(user);
        }

        userRepository.saveAll(users);
        System.out.println("✅ Database seeded with fake data!");
    }

    // Helper methods để tạo dữ liệu ngẫu nhiên
    private GenderEnum randomGender() {
        GenderEnum[] genders = GenderEnum.values();
        return genders[random.nextInt(genders.length)];
    }

    private LocalDate randomBirthday() {
        return LocalDate.now().minusYears(random.nextInt(50) + 18);
    }

    private int randomNumber() {
        return random.nextInt(20) + 1;  // Tạo số ngẫu nhiên từ 1 đến 20
    }


    private TeacherStatusEnum randomTeacherStatus() {
        TeacherStatusEnum[] statuses = TeacherStatusEnum.values();
        return statuses[random.nextInt(statuses.length)];
    }
}
