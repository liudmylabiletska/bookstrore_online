package kristar.projects.service;

import java.util.List;
import kristar.projects.dto.BookDto;
import kristar.projects.dto.BookSearchParametersDto;
import kristar.projects.dto.CreateBookRequestDto;
import kristar.projects.dto.UpdateBookRequestDto;
import kristar.projects.exception.EntityNotFoundException;
import kristar.projects.mapper.BookMapper;
import kristar.projects.model.Book;
import kristar.projects.repository.book.BookRepository;
import kristar.projects.repository.book.BookSpecificationBuilder;
import lombok.RequiredArgsConstructor;
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
    public List<BookDto> getAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
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
    public List<BookDto> search(BookSearchParametersDto searchParameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public BookDto updateById(Long id, UpdateBookRequestDto requestDto) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find book with id "
                        + id + " to update"));
        bookMapper.updateBookFromDto(requestDto, existingBook);
        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toDto(updatedBook);
    }
}
