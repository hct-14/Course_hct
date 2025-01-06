package Course.demo.Repository;

import Course.demo.Entity.Prove;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProveRepository extends JpaRepository<Prove, Integer>, JpaSpecificationExecutor<Prove> {

}
