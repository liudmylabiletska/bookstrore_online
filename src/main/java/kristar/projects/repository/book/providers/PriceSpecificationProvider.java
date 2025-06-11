package kristar.projects.repository.book.providers;

import static kristar.projects.repository.book.BookSpecificationBuilder.PRICE;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import kristar.projects.model.Book;
import kristar.projects.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public abstract class PriceSpecificationProvider implements SpecificationProvider<Book> {
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
}

