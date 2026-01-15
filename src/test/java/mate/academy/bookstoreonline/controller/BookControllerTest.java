package mate.academy.bookstoreonline.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.bookstoreonline.dto.BookDto;
import mate.academy.bookstoreonline.dto.CreateBookRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = {
        "classpath:database/books/remove-books.sql",
        "classpath:database/categories/remove-categories.sql",
        "classpath:database/categories/add-categories.sql",
        "classpath:database/books/add-books-with-categories.sql"
}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {
        "classpath:database/books/remove-books.sql",
        "classpath:database/categories/remove-categories.sql"
}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get book by ID - Success")
    void getBookById_ValidId_ReturnsBookDto() throws Exception {
        BookDto expected = createTestBookDto();

        MvcResult result = mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertThat(actual)
                .usingRecursiveComparison()
                .withEqualsForType((a, b) -> a.compareTo(b) == 0, BigDecimal.class)
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get book by ID - Not Found (Negative)")
    void getBookById_InvalidId_ReturnsNotFound() throws Exception {
        Long nonExistentId = 999L;

        mockMvc.perform(get("/books/" + nonExistentId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Find all books - Success")
    void findAll_ReturnsAllBooks() throws Exception {
        List<BookDto> expected = List.of(createTestBookDto());

        MvcResult result = mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<BookDto> actual = objectMapper.readValue(
                root.get("content").toString(),
                new TypeReference<List<BookDto>>() {}
        );

        assertThat(actual)
                .usingRecursiveComparison()
                .withEqualsForType((a, b) -> a.compareTo(b) == 0, BigDecimal.class)
                .isEqualTo(expected);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create book - Success")
    void createBook_ValidRequest_ReturnsCreatedBook() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle("New Book");
        requestDto.setAuthor("New Author");
        requestDto.setIsbn("isbn-unique-999");
        requestDto.setPrice(BigDecimal.valueOf(50.0));
        requestDto.setCategoryIds(List.of(1L));

        MvcResult result = mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertThat(actual.getTitle()).isEqualTo(requestDto.getTitle());
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update book - Success")
    void updateBook_ValidRequest_ReturnsUpdatedBook() throws Exception {
        CreateBookRequestDto updateRequest = new CreateBookRequestDto();
        updateRequest.setTitle("Updated Title");
        updateRequest.setAuthor("Author");
        updateRequest.setIsbn("isbn-111");
        updateRequest.setPrice(BigDecimal.valueOf(20.0));
        updateRequest.setCategoryIds(List.of(1L));

        MvcResult result = mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        assertThat(actual.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete book - Success")
    void deleteBook_ValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isNotFound());
    }

    private BookDto createTestBookDto() {
        BookDto dto = new BookDto();
        dto.setId(1L);
        dto.setTitle("Test Book");
        dto.setAuthor("Author");
        dto.setIsbn("isbn-111");
        dto.setPrice(new BigDecimal("10.00"));
        dto.setDescription("Description");
        dto.setCoverImage("image.jpg");
        dto.setCategoryIds(List.of(1L));
        return dto;
    }
}
