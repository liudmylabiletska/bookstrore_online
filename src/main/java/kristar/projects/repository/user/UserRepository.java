package kristar.projects.repository.user;

import java.util.Optional;
import kristar.projects.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
