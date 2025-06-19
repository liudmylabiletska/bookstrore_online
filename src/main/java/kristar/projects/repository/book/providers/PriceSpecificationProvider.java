package kristar.projects.repository.book.providers;

import static kristar.projects.repository.book.BookSpecificationBuilder.PRICE;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import kristar.projects.model.Book;
import kristar.projects.repository.book.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public Specification<Book> getSpecificationPrice(BigDecimal minPrice, BigDecimal maxPrice) {

        return (Root<Book> root,
                CriteriaQuery<?> query,
                CriteriaBuilder criteriaBuilder)
                -> criteriaBuilder.between(root.get(PRICE),
                minPrice, maxPrice);
    }

    @Override
    public String getKey() {
        return PRICE;
    }

    @Override
    public Specification<Book> getSpecificationString(String[] params) {
        throw new UnsupportedOperationException("Unsupported operation for prices' filter");
    }
}
