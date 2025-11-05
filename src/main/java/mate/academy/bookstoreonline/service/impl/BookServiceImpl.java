package mate.academy.bookstoreonline.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.dto.BookDto;
import mate.academy.bookstoreonline.dto.BookSearchParametersDto;
import mate.academy.bookstoreonline.dto.CreateBookRequestDto;
import mate.academy.bookstoreonline.dto.UpdateBookRequestDto;
import mate.academy.bookstoreonline.mapper.BookMapper;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.repository.BookRepository;
import mate.academy.bookstoreonline.repository.book.spec.BookSpecificationBuilder;
import mate.academy.bookstoreonline.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto update(Long id, UpdateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
        bookMapper.updateBookFromDto(requestDto, book);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toDto);
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find book with id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void deleteById(Long id) {
            bookRepository.deleteById(id);
        }

    @Override
    public Page<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> bookSpecification = bookSpecificationBuilder
                .build(params);
        return bookRepository.findAll(bookSpecification, pageable)
                .map(bookMapper::toDto);
    }
}
