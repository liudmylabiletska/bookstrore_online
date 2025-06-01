package skg.projects.service;

import java.util.List;
import skg.projects.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> getAll();
}
