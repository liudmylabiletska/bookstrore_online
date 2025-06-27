package kristar.projects.dto.bookdto;

import java.math.BigDecimal;

public record BookSearchParametersDto(
        String[] title,
        String[] author,
        Long[] categoryIds,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {
}
