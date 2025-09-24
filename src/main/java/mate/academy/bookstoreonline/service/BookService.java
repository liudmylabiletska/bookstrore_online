package mate.academy.bookstoreonline.service;

import java.util.List;
import mate.academy.bookstoreonline.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
