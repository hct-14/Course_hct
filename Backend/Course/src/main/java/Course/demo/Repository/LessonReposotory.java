package Course.demo.Repository;

import Course.demo.Entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LessonReposotory extends JpaRepository<Lesson, Integer>, JpaSpecificationExecutor<Lesson> {

}
