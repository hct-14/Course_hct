package Course.demo.Repository;

import Course.demo.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepositoty extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

}
