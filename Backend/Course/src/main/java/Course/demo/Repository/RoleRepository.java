package Course.demo.Repository;

import Course.demo.Entity.Permission;
import Course.demo.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

    boolean existsByRoleName(String roleName); // Đổi thành đúng tên trường

}
