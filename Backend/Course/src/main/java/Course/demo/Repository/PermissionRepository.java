package Course.demo.Repository;

import Course.demo.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionRepository extends JpaRepository<Permission, Integer> , JpaSpecificationExecutor<Permission> {

}
