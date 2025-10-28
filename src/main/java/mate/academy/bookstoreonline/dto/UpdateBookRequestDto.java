package mate.academy.bookstoreonline.dto;

import java.math.BigDecimal;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero; 
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
public class UpdateBookRequestDto {
    @NotNull
    private String title;

    private String author;
    private String isbn;
    @NotNull(message = "Price must not be null.")
    @PositiveOrZero(message = "Price must be positive or zero.")
    private BigDecimal price;

    private String description;

    private String coverImage;
}
