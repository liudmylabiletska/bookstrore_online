package kristar.projects.service;

import java.util.List;

import kristar.projects.dto.BookDto;
import kristar.projects.dto.CreateBookRequestDto;
import kristar.projects.model.Book;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> getAll();

    BookDto finById(Long id);
}
