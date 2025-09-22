package mate.academy.bookstore_online.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore_online.model.Book;
import mate.academy.bookstore_online.repository.BookRepository;
import mate.academy.bookstore_online.service.BookService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}
