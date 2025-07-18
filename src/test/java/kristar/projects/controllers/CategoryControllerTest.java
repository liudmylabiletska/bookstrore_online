package kristar.projects.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import kristar.projects.dto.book.BookDto;
import kristar.projects.dto.book.BookDtoWithoutCategoryIds;
import kristar.projects.dto.category.CategoryRequestDto;
import kristar.projects.dto.category.CategoryResponseDto;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CategoryControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext

    ) throws SQLException {
        teardown(dataSource);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);

            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/add-three-default-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/add-three-category.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/"
                            + "add-category-programming-in-books.sql")
            );
        }
    }

    @SneakyThrows
    @AfterAll
    static void tearDown(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/"
                            + "remove-category-programming-from-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/remove-added-categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/remove-all-books.sql")
            );
        }
    }

    @WithMockUser(username = "dmytro@example.com", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/categories/remove-test-title-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createCategory_ValidRequestDto_Success() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto()
                .setName("Test Title")
                .setDescription("Description");

        CategoryResponseDto expectedDto = new CategoryResponseDto()
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());

        String json = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actualDto = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        assertNotNull(actualDto);

        EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id");
    }

    @WithMockUser(username = "dmytro@example.com", roles = {"ADMIN"})
    @Test
    void getAll_GivenCategoriesInDB_ReturnsAllCategories() throws Exception {
        CategoryResponseDto categoryDto1 = new CategoryResponseDto()
                .setName("Science Fiction")
                .setDescription("Books set in futuristic or imaginative worlds");
        CategoryResponseDto categoryDto2 = new CategoryResponseDto()
                .setName("Mystery")
                .setDescription("Detective stories and whodunits");
        CategoryResponseDto categoryDto3 = new CategoryResponseDto()
                .setName("Biography")
                .setDescription("Life stories of notable individuals");
        CategoryResponseDto categoryDto4 = new CategoryResponseDto()
                .setName("Fantasy")
                .setDescription("Books with magic, mythical creatures, and imaginary worlds");
        CategoryResponseDto categoryDto5 = new CategoryResponseDto()
                .setName("Self-Help")
                .setDescription("Guides for personal growth and motivation");
        CategoryResponseDto categoryDto6 = new CategoryResponseDto()
                .setName("Drama")
                .setDescription("Narrative works focused on realistic");
        CategoryResponseDto categoryDto7 = new CategoryResponseDto()
                .setName("Programming")
                .setDescription("Books dedicated to creating, debugging and optimizing software");
        CategoryResponseDto categoryDto8 = new CategoryResponseDto()
                .setName("Cloud Computing")
                .setDescription("Books covering cloud architecture, services and "
                        + "deployment strategies");
        CategoryResponseDto categoryDto9 = new CategoryResponseDto()
                .setName("Cybersecurity")
                .setDescription("Security principles, practices and threat analysis "
                        + "in software and infrastructure");
        CategoryResponseDto categoryDto10 = new CategoryResponseDto()
                .setName("Adventure")
                .setDescription("Exciting journeys, daring feats, and exploration-driven plots");

        List<CategoryResponseDto> expected = new ArrayList<>();
        expected.add(categoryDto1);
        expected.add(categoryDto2);
        expected.add(categoryDto3);
        expected.add(categoryDto4);
        expected.add(categoryDto5);
        expected.add(categoryDto6);
        expected.add(categoryDto7);
        expected.add(categoryDto8);
        expected.add(categoryDto9);
        expected.add(categoryDto10);

        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode contentNode = root.get("content");

        BookDto[] actualArray = objectMapper.treeToValue(contentNode, BookDto[].class);

        List<BookDto> actual = Arrays.stream(actualArray).toList();

        assertEquals(10, actual.size());

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "dmytro@example.com", roles = {"ADMIN"})
    @Test
    void getCategoryById_ValiId_shouldReturnCategoryDto() throws Exception {
        Long validCategoryId = 7L;
        MvcResult result = mockMvc.perform(get("/categories/{id}", validCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result
                        .getResponse()
                        .getContentAsString(),
                CategoryResponseDto.class
        );

        assertEquals("Programming", actual.getName());
    }

    @WithMockUser(username = "dmytro@example.com", roles = {"ADMIN"})
    @Test
    void updateCategoryById_ValidIdUpdateParams_ReturnsUpdatedCategory() throws Exception {
        Long categoryId = 10L;

        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Updated Name");
        requestDto.setDescription("Updated description only");

        CategoryResponseDto expectedDto = new CategoryResponseDto()
                .setName(requestDto.getName())
                .setDescription(requestDto.getDescription());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(patch("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.description").value("Updated description only"))
                .andReturn();

        CategoryResponseDto actualDto = objectMapper.convertValue(
                JsonPath.read(result.getResponse().getContentAsString(), "$"),
                CategoryResponseDto.class
        );

        assertEquals("Updated Name", actualDto.getName());
        assertEquals("Updated description only", actualDto.getDescription());

        EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id");
    }

    @WithMockUser(username = "dmytro@example.com", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/categories/add-one-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/categories/remove-one-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    void deleteCategoryById_ValidId_DeleteCategory() throws Exception {
        Long categoryId = 11L;

        int statusExpected = 204;

        MvcResult resultActual = mockMvc.perform(delete("/categories/{id}", categoryId))
                .andExpect(status().isNoContent())
                .andReturn();

        int statusActual = resultActual.getResponse().getStatus();

        MvcResult resultActualAfterDeletion = mockMvc.perform(get("/categories/{id}", categoryId))
                .andExpect(status().isNotFound())
                .andReturn();

        int statusExpectedAfterDeletion = 404;

        int statusActualAfterDeletion = resultActualAfterDeletion.getResponse().getStatus();

        assertEquals(statusExpected, statusActual);
        assertEquals(statusExpectedAfterDeletion, statusActualAfterDeletion);
    }

    @WithMockUser(username = "dmytro@example.com", roles = {"ADMIN"})
    @Test
    void getBooksByCategoryId_BooksValidCategoryId_ReturnsListBooks() throws Exception {

        BookDtoWithoutCategoryIds book1 = new BookDtoWithoutCategoryIds();
        book1.setTitle("Java Essentials");
        book1.setAuthor("John Doe");
        book1.setIsbn("111-ABC");
        book1.setPrice(BigDecimal.valueOf(299.99));
        book1.setDescription("Core Java concepts");

        BookDtoWithoutCategoryIds book2 = new BookDtoWithoutCategoryIds();
        book2.setTitle("Spring Boot Starter");
        book2.setAuthor("Jane Smith");
        book2.setIsbn("222-DEF");
        book2.setPrice(BigDecimal.valueOf(399.00));
        book2.setDescription("Build REST APIs with Spring Boot");

        BookDtoWithoutCategoryIds book3 = new BookDtoWithoutCategoryIds();
        book3.setTitle("Docker for Developers");
        book3.setAuthor("Mark Black");
        book3.setIsbn("333-GHI");
        book3.setPrice(BigDecimal.valueOf(199.50));
        book3.setDescription("Practical container usage");

        List<BookDtoWithoutCategoryIds> booksExpected = List.of(book1, book2, book3);

        Long categoryId = 7L;

        MvcResult result = mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .param("page", "0")
                        .param("size", "20")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        List<Map<String, Object>> rawList = JsonPath.read(responseJson, "$.content");

        List<BookDtoWithoutCategoryIds> booksActual = rawList.stream()
                .map(obj -> objectMapper.convertValue(obj, BookDtoWithoutCategoryIds.class))
                .toList();

        booksActual.forEach(book -> book.setId(null));

        assertEquals(booksExpected.size(), booksActual.size());
        assertEquals(booksExpected, booksActual);
    }
}
