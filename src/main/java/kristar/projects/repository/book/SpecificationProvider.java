package kristar.projects.repository.book;

import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    Specification<T> getSpecificationString(String[] params);

    Specification<T> getSpecificationPrice(BigDecimal minPrice, BigDecimal maxPrice);
}
