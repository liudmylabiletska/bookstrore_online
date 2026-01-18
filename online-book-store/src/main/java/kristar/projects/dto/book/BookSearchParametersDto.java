package kristar.projects.dto.book;

import java.math.BigDecimal;
import org.springframework.lang.Nullable;

public record BookSearchParametersDto(
        @Nullable String[] titles,
        @Nullable String[] authors,
        @Nullable Long[] categoryIds,
        @Nullable String[] categoryNames,
        @Nullable BigDecimal minPrice,
        @Nullable BigDecimal maxPrice
) {
    public String[] categoryNames() {
        return categoryNames;
    }
}
