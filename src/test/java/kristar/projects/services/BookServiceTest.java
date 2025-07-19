package kristar.projects.services;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import kristar.projects.services.impl.BookServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookSpecificationBuilder specificationBuilder;

    @Test
    @DisplayName("Verify book save() method")
    public void save_ValidCreateBookRequestDto_ReturnsBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Test Title")
                .setAuthor("Test Author")
                .setIsbn("11111")
                .setPrice(BigDecimal.valueOf(300))
                .setDescription("")
                .setCategoryIds(Set.of(1L, 5L));

        Book book = new Book();
        BookDto expectedDto = new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCategoryIds(requestDto.getCategoryIds());

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when((bookMapper.toDto(book))).thenReturn(expectedDto);

        BookDto actualDto = bookService.save(requestDto);

        assertEquals(expectedDto, actualDto);
        assertEquals("Test Title", actualDto.getTitle());
        assertEquals("11111", actualDto.getIsbn());
        assertEquals(BigDecimal.valueOf(300), actualDto.getPrice());

        verify(bookMapper).toModel(requestDto);
        verify(bookRepository).save(book);
        verify(bookMapper).toDto(book);

        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify findAll_ValidPageable_ReturnsAllCategories() method by user's email")
    public void findAll_ValidUserByEmailPageable_ReturnsAllBooks() {
        Book book = new Book();
        Book book1 = new Book();

        Pageable pageable = PageRequest.of(0, 20);
        String email = "user@example.com";
        BookDto bookDto = new BookDto();
        BookDto bookDtoA = new BookDto();
        List<Book> booksList = List.of(book, book1);
        Page<Book> booksPage = new PageImpl<>(booksList);

        when(bookRepository.findAll(pageable)).thenReturn(booksPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookMapper.toDto(book1)).thenReturn(bookDtoA);

        Page<BookDto> actual = bookService.getAll(email, pageable);

        assertEquals(2, actual.getTotalElements());

        verify(bookRepository).findAll(pageable);
        verify(bookMapper).toDto(book);
        verify(bookMapper).toDto(book1);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify findAll_ValidPageable_ReturnsAllCategories() with empty books list")
    public void getAll_EmptyBooksList_shouldReturnEmptyPage() {
        Pageable pageable = PageRequest.of(0, 20);
        String email = "user@example.com";
        when(bookRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<BookDto> result = bookService.getAll(email, pageable);

        assertTrue(result.isEmpty());

        verify(bookRepository).findAll(pageable);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify findById() method with valid id")
    public void getBookById_ValidId_ReturnsBookDtoByID() {
        Long id = 1L;
        Book book = new Book();
        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setId(id);

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedBookDto);

        BookDto actualBookDto = bookService.findById(id);

        assertEquals(expectedBookDto, actualBookDto);

        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify findById() method with invalid id")
    public void getBookById_InvalidId_ReturnsEmptyBookDto() {
        Long id = 333L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(id)
        );

        String expected = "Can not find book with id " + id;
        String actual = exception.getMessage();

        assertEquals(expected, actual);

        verify(bookRepository).findById(id);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify deletion of book by Id")
    void deleteById_ValidId_ShouldInvokeRepositoryDelete() {
        Long id = 1L;

        Book book = new Book();
        book.setId(id);
        book.setTitle("Dummy Title");
        book.setAuthor("Dummy Author");

        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(id);

        assertDoesNotThrow(() -> bookService.deleteById(id));

        verify(bookRepository).findById(id);
        verify(bookRepository).deleteById(id);
    }

    @Test
    @DisplayName("Verify search books within price range")
    public void getBooksInPriceRange_PriceRange_ReturnsFilteredBooks() {
        Book bookA = new Book();
        bookA.setTitle("Book1");
        bookA.setIsbn("111");
        bookA.setPrice(BigDecimal.valueOf(100));

        Book bookB = new Book();
        bookB.setTitle("Book2");
        bookB.setIsbn("222");
        bookB.setPrice(BigDecimal.valueOf(300));

        BookDto bookDto1 = new BookDto();
        BookDto bookDto2 = new BookDto();

        bookDto1.setTitle(bookA.getTitle());
        bookDto1.setIsbn(bookA.getIsbn());
        bookDto2.setTitle(bookB.getTitle());
        bookDto2.setIsbn(bookB.getIsbn());

        BookSearchParametersDto parametersDto = new BookSearchParametersDto(
                null, null, null, null,
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(300)
        );

        Pageable pageable = PageRequest.of(0, 20);
        Specification<Book> mockspec = mock(Specification.class);

        List<Book> booksList = List.of(bookA, bookB);

        Page<Book> booksPage = new PageImpl<>(booksList);

        when(specificationBuilder.build(parametersDto)).thenReturn(mockspec);
        when(bookRepository.findAll(mockspec, pageable)).thenReturn(booksPage);
        when(bookMapper.toDto(bookA)).thenReturn(bookDto1);
        when(bookMapper.toDto(bookB)).thenReturn(bookDto2);

        Page<BookDto> actual = bookService.search(parametersDto, pageable);

        assertEquals(2, actual.getTotalElements());
        assertEquals("Book1", actual.getContent().get(0).getTitle());
        assertEquals("Book2", actual.getContent().get(1).getTitle());

        verify(specificationBuilder).build(parametersDto);
        verify(bookRepository).findAll(mockspec, pageable);
        verify(bookMapper).toDto(bookA);
        verify(bookMapper).toDto(bookB);
    }

    @Test
    @DisplayName("Verify updateById() updates book and returns BookDto")
    public void updateById_validId_shouldUpdateBook() {
        Long id = 1L;

        UpdateBookRequestDto updateRequestDto = new UpdateBookRequestDto();
        updateRequestDto.setTitle("Updated Title");

        Book existingBook = new Book();
        existingBook.setId(id);
        existingBook.setTitle("Old Title");

        Book updatedBook = new Book();
        updatedBook.setId(id);
        updatedBook.setTitle(updateRequestDto.getTitle());

        BookDto updatedDto = new BookDto();
        updatedDto.setId(id);
        updatedDto.setTitle(updatedBook.getTitle());

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        doAnswer(invocation -> {
            existingBook.setTitle(updateRequestDto.getTitle());
            return null;
        }).when(bookMapper).updateBookFromDto(existingBook, updateRequestDto);
        when(bookRepository.save(existingBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(updatedDto);

        BookDto actualDto = bookService.updateById(id, updateRequestDto);

        assertEquals("Updated Title", actualDto.getTitle());
        verify(bookRepository).findById(id);
        verify(bookMapper).updateBookFromDto(existingBook, updateRequestDto);
        verify(bookRepository).save(existingBook);
        verify(bookMapper).toDto(updatedBook);
    }

    @Test
    @DisplayName("Verify findBooksByCategoryId() method")
    public void findBooksByCategoryId_validCategoryId_ReturnsBooks() {
        Long categoryId = 2L;
        Pageable pageable = PageRequest.of(0, 20);

        Book book = new Book();
        book.setTitle("Found book with param categoryId");

        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        dto.setTitle(book.getTitle());

        Page<Book> bookPage = new PageImpl<>(List.of(book));
        when(bookRepository.findAllByCategoryId(categoryId, pageable)).thenReturn(bookPage);
        when(bookMapper.toDtoWithoutCategories(book)).thenReturn(dto);

        Page<BookDtoWithoutCategoryIds> actual = bookService
                .findBooksByCategoryId(categoryId, pageable);

        assertEquals(1, actual.getTotalElements());
        assertEquals("Found book with param categoryId", actual.getContent().get(0).getTitle());

        verify(bookRepository).findAllByCategoryId(categoryId, pageable);
        verify(bookMapper).toDtoWithoutCategories(book);
    }
}
