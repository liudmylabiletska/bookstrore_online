package skg.projects.repository;

import java.util.List;
import skg.projects.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
