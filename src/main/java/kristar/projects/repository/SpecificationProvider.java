package kristar.projects.repository;

import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    default Specification<T> getSpecificationString(String[] params) {
        return null;
    }

    default Specification<T> getSpecificationPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        return null;
    }
}
