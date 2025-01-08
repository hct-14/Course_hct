package Course.demo.Repository;

import Course.demo.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Integer> , JpaSpecificationExecutor<Permission> {

    boolean existsByName(String name);
    List<Permission> findByNameIn(List<Permission> permissionNames);

}
