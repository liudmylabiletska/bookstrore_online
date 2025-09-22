package mate.academy.bookstrore_online.repository;

import java.util.List;
import mate.academy.bookstrore_online.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
