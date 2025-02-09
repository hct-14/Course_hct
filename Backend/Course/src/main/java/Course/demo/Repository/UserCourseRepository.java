package Course.demo.Repository;

import Course.demo.Entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCourseRepository extends JpaRepository<UserCourse, Integer> {
    List<Integer> findCourseIdsByUserId(int id);

    List<Integer> findTeacherCourseIdsByUserId(int id);
}
