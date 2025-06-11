package kristar.projects.service;

import java.util.List;
import kristar.projects.dto.BookDto;
import kristar.projects.dto.BookSearchParametersDto;
import kristar.projects.dto.CreateBookRequestDto;
import kristar.projects.dto.UpdateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> getAll();

    BookDto findById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);

    BookDto updateById(Long id, UpdateBookRequestDto requestDto);
}
