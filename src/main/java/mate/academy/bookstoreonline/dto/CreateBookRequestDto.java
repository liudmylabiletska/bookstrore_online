package mate.academy.bookstoreonline.dto;

import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Title is required")
    private String author;

    @NotNull(message = "Price is required.")
    @PositiveOrZero(message = "Price must be positive or zero.")
    private BigDecimal price;

    @NotBlank(message = "Invalid ISBN format")
    private String isbn;

    private String description;
    private String coverImage;
}
