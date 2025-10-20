package mate.academy.bookstoreonline.repository;

import mate.academy.bookstoreonline.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

}
