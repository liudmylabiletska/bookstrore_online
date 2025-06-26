package kristar.projects.repository.book.providers;

import static kristar.projects.repository.book.BookSpecificationBuilder.CATEGORY;

import java.math.BigDecimal;
import java.util.Arrays;
import kristar.projects.model.Book;
import kristar.projects.repository.book.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CategorySpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public Specification<Book> getSpecificationLong(Long[] categoryIds) {
        return (root, query, criteriaBuilder)
                -> root.get(CATEGORY).in(Arrays.stream(categoryIds).toArray());
    }

    @Override
    public String getKey() {
        return CATEGORY;
    }

    @Override
    public Specification<Book> getSpecificationString(String[] params) {
        throw new UnsupportedOperationException("Unsupported operation for filter by category");
    }

    @Override
    public Specification<Book> getSpecificationPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        throw new UnsupportedOperationException("Unsupported operation for filter by category");
    }
}
