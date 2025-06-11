package kristar.projects.service;

import java.util.List;
import kristar.projects.dto.BookDto;
import kristar.projects.dto.BookSearchParametersDto;
import kristar.projects.dto.CreateBookRequestDto;
import kristar.projects.dto.UpdateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> getAll(Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);

    BookDto updateById(Long id, UpdateBookRequestDto requestDto);
}
