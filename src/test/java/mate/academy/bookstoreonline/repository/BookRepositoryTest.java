package mate.academy.bookstoreonline.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.model.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("findAllByCategoriesId should return books linked to this category")
    void testFindByCategoriesId() {
        Category category = new Category();
        category.setName("Fiction");
        Category savedCategory = categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Author");
        book.setIsbn("123-456-789");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setCategories(new HashSet<>());
        book.getCategories().add(savedCategory);

        bookRepository.save(book);
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> result = bookRepository.findAllByCategoriesId(savedCategory.getId(), pageable).getContent();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Book");
    }

    @Test
    @DisplayName("findAllByCategoriesId should return empty list when category has no books")
    void testFindByCategoryId_NoBooks() {
        Category category = new Category();
        category.setName("Empty");
        Category savedCategory = categoryRepository.save(category);

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> result = bookRepository.findAllByCategoriesId(savedCategory.getId(), pageable).getContent();

        assertThat(result).isEmpty();
    }
}
