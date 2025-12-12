package mate.academy.bookstoreonline.dto.book;

import lombok.Data;
import java.math.BigDecimal;

public class BookDtoWithoutCategoryIds {
    private String title;

    private String author;

    private String isbn;

    private BigDecimal price;

    private String description;

    private String coverImage;
}
