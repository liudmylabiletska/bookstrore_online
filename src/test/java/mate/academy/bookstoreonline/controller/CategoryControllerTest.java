package mate.academy.bookstoreonline.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import mate.academy.bookstoreonline.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreonline.dto.book.CategoryDto;
import mate.academy.bookstoreonline.dto.book.CategoryRequestDto;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.model.Category;
import mate.academy.bookstoreonline.repository.BookRepository;
import mate.academy.bookstoreonline.repository.CategoryRepository;
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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create category should return 201 Created")
    void createCategory_shouldCreate() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setTitle("Tech");
        requestDto.setAuthor("Admin");
        requestDto.setIsbn("isbn-cat-123");
        requestDto.setPrice(BigDecimal.ZERO);
        requestDto.setDescription("Description");

        String json = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))) // Перетворюємо в JSON
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CategoryDto actual = objectMapper.readValue(json, CategoryDto.class);
        assertThat(actual.getName()).isEqualTo("Tech");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all categories")
    void getAll_shouldReturnCategories() throws Exception {
        Category category = new Category();
        category.setName("Category1");
        categoryRepository.save(category);

        String json = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode root = objectMapper.readTree(json);
        JsonNode contentNode = root.has("content") ? root.get("content") : root;

        List<CategoryDto> categories = objectMapper.readValue(
                contentNode.toString(),
                new TypeReference<List<CategoryDto>>() {
                }
        );

        assertThat(categories).isNotEmpty();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update category")
    void updateCategory() throws Exception {
        Category category = new Category();
        category.setName("Old");
        Category savedCategory = categoryRepository.save(category);
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setTitle("Updated Category");
        requestDto.setAuthor("Some Author");
        requestDto.setIsbn("isbn-123-update");
        requestDto.setPrice(BigDecimal.TEN);
        requestDto.setDescription("New Desc");

        mockMvc.perform(put("/categories/" + savedCategory.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        Category updated = categoryRepository.findById(savedCategory.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Updated Category");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get books by category")
    void getBooksByCategory_shouldReturnBooks() throws Exception {
        Category category = new Category();
        category.setName("Science");
        Category savedCategory = categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Quantum Physics");
        book.setAuthor("Einstein");
        book.setIsbn("isbn-unique-999");
        book.setPrice(BigDecimal.TEN);
        book.setCategories(new HashSet<>(Set.of(savedCategory)));
        bookRepository.save(book);

        String json = mockMvc.perform(get("/categories/" + savedCategory.getId() + "/books"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode root = objectMapper.readTree(json);
        JsonNode contentNode = root.has("content") ? root.get("content") : root;

        List<BookDtoWithoutCategoryIds> books = objectMapper.readValue(
                contentNode.toString(),
                new TypeReference<List<BookDtoWithoutCategoryIds>>() {
                }
        );

        assertThat(books).isNotEmpty();
        assertThat(books.get(0).getTitle()).isEqualTo("Quantum Physics");
    }
}
