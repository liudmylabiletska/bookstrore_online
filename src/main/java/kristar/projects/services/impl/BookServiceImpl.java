package kristar.projects.services.impl;

import kristar.projects.dto.book.BookDto;
import kristar.projects.dto.book.BookDtoWithoutCategoryIds;
import kristar.projects.dto.book.BookSearchParametersDto;
import kristar.projects.dto.book.CreateBookRequestDto;
import kristar.projects.dto.book.UpdateBookRequestDto;
import kristar.projects.exception.EntityNotFoundException;
import kristar.projects.mapper.BookMapper;
import kristar.projects.model.Book;
import kristar.projects.repository.book.BookRepository;
import kristar.projects.repository.book.BookSpecificationBuilder;
import kristar.projects.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    @Transactional
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public Page<BookDto> getAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public Page<BookDto> getAll(String email, Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find book with id " + id));

        return bookMapper.toDto(book);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> search(BookSearchParametersDto searchParameters, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toDto);
    }

    @Override
    @Transactional
    public BookDto updateById(Long id, UpdateBookRequestDto requestDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find book with id "
                        + id + " to update"));
        bookMapper.updateBookFromDto(existingBook, requestDto);
        return bookMapper.toDto(bookRepository.save(existingBook));
    }

    @Override
    public Page<BookDtoWithoutCategoryIds> findBooksByCategoryId(Long id, Pageable pageable) {
        Page<Book> allBooksByCategoryId = bookRepository.findAllByCategoryId(id, pageable);
        return allBooksByCategoryId.map((book -> bookMapper.toDtoWithoutCategories(book)));
    }
}
