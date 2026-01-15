package mate.academy.bookstoreonline.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import mate.academy.bookstoreonline.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreonline.dto.book.CategoryDto;
import mate.academy.bookstoreonline.dto.book.CategoryRequestDto;
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
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Create category - Success")
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequest_ReturnsCreatedCategory() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setTitle("Sci-Fi");
        requestDto.setDescription("Science Fiction");

        MvcResult result = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(requestDto);
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get all categories - Success")
    @Sql(scripts = "classpath:database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_ReturnsAllCategories() throws Exception {
        CategoryDto fictionDto = new CategoryDto();
        fictionDto.setId(1L);
        fictionDto.setName("Fiction");
        fictionDto.setDescription("Fiction books");

        List<CategoryDto> expected = List.of(fictionDto);

        MvcResult result = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode contentNode = root.has("content") ? root.get("content") : root;

        List<CategoryDto> actual = objectMapper.readValue(
                contentNode.toString(),
                new TypeReference<List<CategoryDto>>() {}
        );

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Update category - Success")
    @Sql(scripts = "classpath:database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateCategory_ValidId_ReturnsUpdatedDto() throws Exception {
        CategoryRequestDto updateRequest = new CategoryRequestDto();
        updateRequest.setTitle("Updated Name");
        updateRequest.setDescription("Updated Desc");

        MvcResult result = mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        assertThat(actual.getName()).isEqualTo("Updated Name");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get books by category ID - Success")
    @Sql(scripts = {
            "classpath:database/categories/add-categories.sql",
            "classpath:database/books/add-books-with-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books/remove-books.sql",
            "classpath:database/categories/remove-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBooksByCategoryId_ValidId_ReturnsList() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/1/books"))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode contentNode = root.has("content") ? root.get("content") : root;

        List<BookDtoWithoutCategoryIds> actual = objectMapper.readValue(
                contentNode.toString(),
                new TypeReference<List<BookDtoWithoutCategoryIds>>() {}
        );

        assertThat(actual).isNotEmpty();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get category by ID - Success")
    @Sql(scripts = "classpath:database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getById_ValidId_ReturnsCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(get("/categories/1"))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getName()).isEqualTo("Fiction");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Delete category - Success")
    @Sql(scripts = "classpath:database/categories/add-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-categories.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteCategory_ValidId_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/categories/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Get category by ID - Not Found")
    void getById_InvalidId_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/categories/999"))
                .andExpect(status().isNotFound());
    }
}
