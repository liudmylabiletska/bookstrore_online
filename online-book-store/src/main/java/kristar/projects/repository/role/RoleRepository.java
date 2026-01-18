package kristar.projects.repository.role;

import java.util.Optional;
import kristar.projects.model.Role;
import kristar.projects.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
