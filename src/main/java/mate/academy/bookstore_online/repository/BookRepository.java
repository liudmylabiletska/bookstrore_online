package mate.academy.bookstore_online.repository;

import java.util.List;
import mate.academy.bookstore_online.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
