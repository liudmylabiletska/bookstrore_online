package kristar.projects.repository.book.providers;

import static kristar.projects.repository.book.BookSpecificationBuilder.TITLE;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.Arrays;
import kristar.projects.model.Book;
import kristar.projects.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public Specification<Book> getSpecificationString(String[] params) {

        return new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return root.get(TITLE).in(Arrays.stream(params).toArray());
            }
        };
    }

    @Override
    public Specification<Book> getSpecificationPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        throw new UnsupportedOperationException("Unsupported operation for titles' filter");
    }

    @Override
    public String getKey() {
        return TITLE;
    }
}
