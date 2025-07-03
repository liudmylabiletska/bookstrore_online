package kristar.projects.services;

import kristar.projects.dto.book.BookDto;
import kristar.projects.dto.book.BookDtoWithoutCategoryIds;
import kristar.projects.dto.book.BookSearchParametersDto;
import kristar.projects.dto.book.CreateBookRequestDto;
import kristar.projects.dto.book.UpdateBookRequestDto;
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

    Page<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long id, Pageable pageable);
}
