package kristar.projects.service;

import kristar.projects.dto.bookdto.BookDto;
import kristar.projects.dto.bookdto.BookSearchParametersDto;
import kristar.projects.dto.bookdto.CreateBookRequestDto;
import kristar.projects.dto.bookdto.UpdateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> getAll(Pageable pageable);

    Page<BookDto> getAll(String email, Pageable pageable);

    BookDto findById(Long id);

    void deleteById(Long id);

    Page<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable);

    BookDto updateById(Long id, UpdateBookRequestDto requestDto);
}
