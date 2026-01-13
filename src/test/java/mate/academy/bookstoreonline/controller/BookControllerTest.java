package mate.academy.bookstoreonline.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import mate.academy.bookstoreonline.dto.BookDto;
import mate.academy.bookstoreonline.dto.CreateBookRequestDto;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.model.Category;
import mate.academy.bookstoreonline.repository.BookRepository;
import mate.academy.bookstoreonline.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test") 
@AutoConfigureMockMvc
@Transactional
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category savedCategory;

    @BeforeEach
    void setup() {
        Category category = new Category();
        category.setName("Fantasy " + System.currentTimeMillis());
        category.setDescription("Test Description");
        savedCategory = categoryRepository.save(category);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get book by ID returns DTO")
    void getBookById_returnsBookDto() throws Exception {
        Book book = createBook("Test Book", "isbn-111");
        Book savedBook = bookRepository.save(book);

        String json = mockMvc.perform(get("/books/" + savedBook.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        BookDto actual = objectMapper.readValue(json, BookDto.class);
        assertThat(actual.getTitle()).isEqualTo("Test Book");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Find all books with pagination")
    void findAll_shouldReturnPagedBooks() throws Exception {
        bookRepository.save(createBook("Book X", "isbn-222"));

        String json = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode root = objectMapper.readTree(json);
        JsonNode contentNode = root.has("content") ? root.get("content") : root;

        List<BookDto> books = objectMapper.readValue(
                contentNode.toString(),
                new TypeReference<List<BookDto>>() {}
        );

        assertThat(books).isNotEmpty();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create book works correctly")
    void createBook_shouldCreateNewBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("New Tech Book");
        requestDto.setAuthor("Author A");
        requestDto.setIsbn("isbn-new-777");
        requestDto.setPrice(BigDecimal.valueOf(50));
        requestDto.setCategoryIds(List.of(savedCategory.getId()));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    private Book createBook(String title, String isbn) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor("Author");
        book.setIsbn(isbn);
        book.setPrice(BigDecimal.TEN);
        book.setCategories(new HashSet<>(List.of(savedCategory)));
        return book;
    }
}
