package kristar.projects.service;

import java.util.List;
import kristar.projects.dto.BookDto;
import kristar.projects.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> getAll();

    BookDto findById(Long id);

    void deleteById(Long id);
}
