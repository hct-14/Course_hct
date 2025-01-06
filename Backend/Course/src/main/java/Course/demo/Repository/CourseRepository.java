package Course.demo.Repository;

import Course.demo.Entity.Course;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Integer> , JpaSpecificationExecutor<Course> {

}
