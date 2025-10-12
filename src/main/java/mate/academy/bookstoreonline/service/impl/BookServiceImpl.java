package mate.academy.bookstoreonline.service.impl;

import java.util.List;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.dto.BookDto;
import mate.academy.bookstoreonline.dto.CreateBookRequestDto;
import mate.academy.bookstoreonline.mapper.BookMapper;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.repository.BookRepository;
import mate.academy.bookstoreonline.service.BookService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public BookDto findById(Long id) {
        return bookRepository.findById(id).stream()
                .map(bookMapper::toDto)
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Can not find book with id: " + id));
    }
}
