package kristar.projects.dto.bookdto;

import java.math.BigDecimal;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        Long[] categoryIds,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {
}
