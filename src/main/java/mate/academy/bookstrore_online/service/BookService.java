package mate.academy.bookstrore_online.service;

import java.util.List;
import mate.academy.bookstrore_online.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
