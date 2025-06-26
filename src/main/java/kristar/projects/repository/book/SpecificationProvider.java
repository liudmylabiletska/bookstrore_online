package kristar.projects.repository.book;

import java.math.BigDecimal;
import org.springframework.data.jpa.domain.Specification;

public abstract interface SpecificationProvider<T> {
    public String getKey();

    abstract Specification<T> getSpecificationString(String[] params);

    abstract Specification<T> getSpecificationPrice(BigDecimal minPrice, BigDecimal maxPrice);

    abstract Specification<T> getSpecificationLong(Long[] ids);
}
