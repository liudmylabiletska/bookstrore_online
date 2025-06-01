package kristar.projects.repository;

import java.util.List;
import java.util.Optional;

import kristar.projects.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);
}
