package mate.academy.bookstoreonline.service;

import mate.academy.bookstoreonline.dto.BookDto;
import mate.academy.bookstoreonline.dto.BookSearchParametersDto;
import mate.academy.bookstoreonline.dto.CreateBookRequestDto;
import mate.academy.bookstoreonline.dto.UpdateBookRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, UpdateBookRequestDto requestDto);

    void deleteById(Long id);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);

    Page<BookDto> findAllBooksByCategoryId(Long id, Pageable pageable);
}
