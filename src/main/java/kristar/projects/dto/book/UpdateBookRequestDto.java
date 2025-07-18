package kristar.projects.dto.book;

import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class UpdateBookRequestDto {
    @Nullable
    private String title;
    @Nullable
    private String author;
    @Nullable
    private String isbn;
    @Nullable
    private BigDecimal price;
    @Nullable
    private String description;
    @Nullable
    private String coverImage;
    @Nullable
    private Set<Long> categoryIds;
}
