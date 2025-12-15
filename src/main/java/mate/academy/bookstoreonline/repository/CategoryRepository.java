package mate.academy.bookstoreonline.repository;

import mate.academy.bookstoreonline.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
