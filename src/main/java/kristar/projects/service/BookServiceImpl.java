package kristar.projects.service;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;
import kristar.projects.dto.BookDto;
import kristar.projects.dto.CreateBookRequestDto;
import kristar.projects.mapper.BookMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kristar.projects.model.Book;
import kristar.projects.repository.BookRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
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
    public BookDto finById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can not find book with id" + id));
        return bookMapper.toDto(book);
    };

}
