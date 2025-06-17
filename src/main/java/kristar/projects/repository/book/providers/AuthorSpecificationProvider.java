package kristar.projects.repository.book.providers;

import static kristar.projects.repository.book.BookSpecificationBuilder.AUTHOR;

import java.math.BigDecimal;
import java.util.Arrays;
import kristar.projects.model.Book;
import kristar.projects.repository.book.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public Specification<Book> getSpecificationString(String[] params) {

        return (root, query, criteriaBuilder)
                -> root.get(AUTHOR).in(Arrays.stream(params).toArray());
    }

    @Override
    public Specification<Book> getSpecificationPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        throw new UnsupportedOperationException("Unsupported operation for authors' filter");
    }

    @Override
    public String getKey() {
        return AUTHOR;
    }
}
