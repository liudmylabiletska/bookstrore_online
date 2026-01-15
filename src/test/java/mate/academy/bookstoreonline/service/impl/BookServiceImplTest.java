package mate.academy.bookstoreonline.service.impl;

import mate.academy.bookstoreonline.dto.BookDto;
import mate.academy.bookstoreonline.dto.CreateBookRequestDto;
import mate.academy.bookstoreonline.exception.EntityNotFoundException;
import mate.academy.bookstoreonline.dto.UpdateBookRequestDto;
import mate.academy.bookstoreonline.mapper.BookMapper;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.repository.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void findById_validId_returnsBookDto() {
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        BookDto expectedDto = new BookDto();
        expectedDto.setId(id);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedDto);
        BookDto actual = bookService.findById(id);

        assertEquals(expectedDto, actual);
        verify(bookRepository).findById(id);
    }

    @Test
    void save_success() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        Book book = new Book();
        Book savedBook = new Book();
        BookDto expectedDto = new BookDto();

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toDto(savedBook)).thenReturn(expectedDto);
        BookDto actual = bookService.save(requestDto);

        assertEquals(expectedDto, actual);
        verify(bookRepository).save(book);
    }

    @Test
    void update_validId_updatesBook() {
        Long id = 1L;
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto();
        requestDto.setTitle("Updated Title");

        Book existingBook = new Book();
        existingBook.setId(id);

        Book savedBook = new Book();
        savedBook.setId(id);

        BookDto expectedDto = new BookDto();

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        doNothing().when(bookMapper).updateBookFromDto(requestDto, existingBook);
        when(bookRepository.save(existingBook)).thenReturn(savedBook);
        when(bookMapper.toDto(savedBook)).thenReturn(expectedDto);

        BookDto actual = bookService.update(id, requestDto);

        assertEquals(expectedDto, actual);
        verify(bookRepository).save(existingBook);
    }

    @Test
    void deleteById_success() {
        Long id = 1L;
        bookService.deleteById(id);
        verify(bookRepository).deleteById(id);
    }

    @Test
    @DisplayName("findById should throw exception when book not found")
    void findById_invalidId_throwsException() {
        Long id = 100L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.findById(id));
        verify(bookRepository).findById(id);
        verifyNoInteractions(bookMapper);
    }

    @Test
    @DisplayName("update should throw exception when book not found")
    void update_invalidId_throwsException() {
        Long id = 100L;
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto();
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.update(id, requestDto));
    }
}
