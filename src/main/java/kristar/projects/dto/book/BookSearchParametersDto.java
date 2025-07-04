package kristar.projects.dto.book;

import java.math.BigDecimal;

public record BookSearchParametersDto(
        String[] titles,
        String[] authors,
        Long[] categoryIds,
        String[] categoryNames,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {
    public String[] categoryNames() {
        return categoryNames;
    }
}
