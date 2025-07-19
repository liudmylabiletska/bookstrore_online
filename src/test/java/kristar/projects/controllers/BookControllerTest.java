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
import java.util.Set;
import javax.sql.DataSource;
import kristar.projects.dto.book.BookDto;
import kristar.projects.dto.book.BookSearchParametersDto;
import kristar.projects.dto.book.CreateBookRequestDto;
import kristar.projects.dto.book.UpdateBookRequestDto;
import kristar.projects.repository.book.BookRepository;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    protected static MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
        }
    }

    @WithMockUser(username = "dmytro@example.com", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/add-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getAll_GivenBooksInCatalog_ReturnsAllBooks() throws Exception {

        BookDto bookDto1 = new BookDto()
                .setTitle("Java Essentials")
                .setAuthor("John Doe")
                .setIsbn("111-ABC")
                .setPrice(BigDecimal.valueOf(299.99))
                .setDescription("Core Java concepts");

        BookDto bookDto2 = new BookDto()
                .setTitle("Spring Boot Starter")
                .setAuthor("Jane Smith")
                .setIsbn("222-DEF")
                .setPrice(BigDecimal.valueOf(399.00))
                .setDescription("Build REST APIs with Spring Boot");

        BookDto bookDto3 = new BookDto()
                .setTitle("Docker for Developers")
                .setAuthor("Mark Black")
                .setIsbn("333-GHI")
                .setPrice(BigDecimal.valueOf(199.50))
                .setDescription("Practical container usage");

        List<BookDto> expected = new ArrayList<>();
        expected.add(bookDto1);
        expected.add(bookDto2);
        expected.add(bookDto3);

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        JsonNode contentNode = root.get("content");

        BookDto[] actualArray = objectMapper.treeToValue(contentNode, BookDto[].class);

        List<BookDto> actual = Arrays.stream(actualArray).toList();

        assertEquals(3, actual.size());

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "dmytro@example.com", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/add-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getBookById_ValiId_shouldReturnBookDto() throws Exception {
        Long bookId = 1L;

        MvcResult result = mockMvc.perform(get("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookDto.class);

        assertEquals("Java Essentials", actual.getTitle());
        assertEquals("John Doe", actual.getAuthor());

    }

    @WithMockUser(username = "dmytro@example.com", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/remove-test-title-book.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Test Title")
                .setAuthor("Test Author")
                .setIsbn("11111")
                .setPrice(BigDecimal.valueOf(300))
                .setDescription("")
                .setCategoryIds(Set.of(1L, 5L));

        BookDto expectedDto = new BookDto()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCategoryIds(requestDto.getCategoryIds());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
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
    @Sql(
            scripts = "classpath:database/books/add-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void deleteBookById_ValidId_DeleteBook() throws Exception {
        Long bookId = 2L;

        int statusExpected = 204;

        MvcResult resultActual = mockMvc.perform(delete("/books/{id}", bookId))
                .andExpect(status().isNoContent())
                .andReturn();

        int statusActual = resultActual.getResponse().getStatus();

        MvcResult resultActualAfterDeletion = mockMvc.perform(get("/books/{id}", bookId))
                .andExpect(status().isNotFound())
                .andReturn();

        int statusExpectedAfterDeletion = 404;

        int statusActualAfterDeletion = resultActualAfterDeletion.getResponse().getStatus();

        assertEquals(statusExpected, statusActual);
        assertEquals(statusExpectedAfterDeletion, statusActualAfterDeletion);
    }

    @WithMockUser(username = "dmytro@example.com", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/add-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void searchBooks_ByTitleKeyword_ReturnsMatches() throws Exception {
        BookSearchParametersDto parametersDto = new BookSearchParametersDto(new String[]{"Docker"},
                null, null, null, null, null);

        BookDto expectedDto = new BookDto()
                .setTitle("Docker for Developers")
                .setAuthor("Mark Black")
                .setIsbn("333-GHI")
                .setPrice(BigDecimal.valueOf(199.50))
                .setDescription("Practical container usage");

        MvcResult result = mockMvc.perform(get("/books/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("title", "Docker"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Docker for Developers"))
                .andReturn();

        Object read = JsonPath.read(result.getResponse().getContentAsString(),
                "$.content[0]");

        BookDto actualDto = objectMapper.convertValue(read, BookDto.class);

        assertNotNull(actualDto);

        EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id, categoryIds,"
                + " coverImage, description");
    }

    @WithMockUser(username = "dmytro@example.com", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/books/add-three-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/books/remove-all-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void updateById_ValidIdUpdateParams_ReturnsUpdatedBook() throws Exception {
        Long bookId = 3L;

        UpdateBookRequestDto partialUpdate = new UpdateBookRequestDto();
        partialUpdate.setPrice(BigDecimal.valueOf(230.99));
        partialUpdate.setDescription("Updated description only");

        BookDto expectedDto = new BookDto()
                .setTitle("Docker for Developers")
                .setAuthor("Mark Black")
                .setIsbn("333-GHI")
                .setPrice(partialUpdate.getPrice())
                .setDescription(partialUpdate.getDescription());

        String jsonRequest = objectMapper.writeValueAsString(partialUpdate);

        MvcResult result = mockMvc.perform(patch("/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(BigDecimal.valueOf(230.99)))
                .andExpect(jsonPath("$.description").value("Updated description only"))
                .andReturn();

        BookDto actualDto = objectMapper.convertValue(
                JsonPath.read(result.getResponse().getContentAsString(), "$"),
                BookDto.class
        );

        assertEquals(BigDecimal.valueOf(230.99), actualDto.getPrice());
        assertEquals("Updated description only", actualDto.getDescription());

        EqualsBuilder.reflectionEquals(expectedDto, actualDto, "id, categoryIds, coverImage");
    }
}
