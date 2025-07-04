package kristar.projects.repository.book.providers;

import java.math.BigDecimal;
import kristar.projects.dto.book.BookSearchParametersDto;
import kristar.projects.exception.DataProcessingException;
import kristar.projects.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("price")
public class PriceSpecificationProvider implements UnifiedSpecificationProvider<Book> {
    public static final String PRICE_FIELD = "price";

    @Override
    public String getKey() {
        return PRICE_FIELD;
    }

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        BigDecimal min = searchParametersDto.minPrice();
        BigDecimal max = searchParametersDto.maxPrice();

        if (min == null || max == null) {
            throw new DataProcessingException("Search parameter by price is empty");
        }

        if (min.compareTo(BigDecimal.ZERO) < 0 || max.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Prices must be non-negative.");
        }

        if (max.compareTo(min) < 0) {
            throw new IllegalArgumentException("Min price must be "
                    + "less than or equal to max price.");
        }

        return (root, query, cb) -> cb.between(root.get(PRICE_FIELD), min, max);
    }
}

