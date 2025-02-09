//package Course.demo.Seeder;
//
//import Course.demo.Entity.Permission;
//import Course.demo.Entity.Role;
//import Course.demo.Repository.PermissionRepository;
//import Course.demo.Repository.RoleRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Service
//public class RoleSeeder {
//
//    private final RoleRepository roleRepository;
//    private final PermissionRepository permissionRepository;
//    private final Random random = new Random();
//
//    public RoleSeeder(RoleRepository roleRepository, PermissionRepository permissionRepository) {
//        this.roleRepository = roleRepository;
//        this.permissionRepository = permissionRepository;
//    }
//
//    // Hàm này có thể gọi thủ công từ Controller hoặc bất kỳ Service nào khác
//    public void seedData() {
//        seedPermissions();
//        seedRolesWithPermissions();
//    }
//
//    // Seed dữ liệu Permission
//    private void seedPermissions() {
//        if (permissionRepository.count() == 0) {
//            List<Permission> permissions = List.of(
//                    new Permission(0, "READ", "Quyền đọc dữ liệu", null),
//                    new Permission(0, "WRITE", "Quyền ghi dữ liệu", null),
//                    new Permission(0, "DELETE", "Quyền xóa dữ liệu", null),
//                    new Permission(0, "UPDATE", "Quyền cập nhật dữ liệu", null)
//            );
//            permissionRepository.saveAll(permissions);
//            System.out.println("✅ Seeded permissions successfully!");
//        }
//    }
//
//    // Seed dữ liệu Role và gán Permission
//    @Transactional
//    private void seedRolesWithPermissions() {
//        List<Role> roles = roleRepository.findAll();
//        List<Permission> permissions = permissionRepository.findAll();
//
//        if (roles.isEmpty()) {
//            roles = List.of(
//                    new Role(0, "ADMIN", new ArrayList<>(), new ArrayList<>()),
//                    new Role(0, "TEACHER", new ArrayList<>(), new ArrayList<>()),
//                    new Role(0, "STUDENT", new ArrayList<>(), new ArrayList<>())
//            );
//            roleRepository.saveAll(roles);
//            System.out.println("✅ Seeded roles successfully!");
//        }
//
//        for (Role role : roles) {
//            if (role.getPermissions().isEmpty()) {
//                int numberOfPermissions = random.nextInt(permissions.size()) + 1;
//                List<Permission> assignedPermissions = new ArrayList<>();
//
//                for (int i = 0; i < numberOfPermissions; i++) {
//                    Permission randomPermission = permissions.get(random.nextInt(permissions.size()));
//                    if (!assignedPermissions.contains(randomPermission)) {
//                        assignedPermissions.add(randomPermission);
//                    }
//                }
//
//                if (!assignedPermissions.isEmpty()) {
//                    role.setPermissions(assignedPermissions);
//                    roleRepository.save(role);
//                    System.out.println("✅ Assigned permissions to role: " + role.getRoleName());
//                }
//            }
//        }
//    }
//}
