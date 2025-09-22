package mate.academy.bookstore_online.service;

import java.util.List;
import mate.academy.bookstore_online.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
