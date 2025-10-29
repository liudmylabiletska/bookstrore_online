package mate.academy.bookstoreonline.service;

import java.util.List;
import mate.academy.bookstoreonline.dto.BookDto;
import mate.academy.bookstoreonline.dto.BookSearchParametersDto;
import mate.academy.bookstoreonline.dto.CreateBookRequestDto;
import mate.academy.bookstoreonline.dto.UpdateBookRequestDto;

public interface BookService {

    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    BookDto update(Long id, UpdateBookRequestDto requestDto);

    void delete(Long id);

    List<BookDto> search(BookSearchParametersDto searchParameters);
    List<BookDto> search(BookSearchParametersDto bookSearchParametersDto);
}
