package mate.academy.bookstoreonline.dto.book;

import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class CategoryRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "ISNB is required")
    private String isbn;

    @NotNull(message = "Prise is required")
    @DecimalMin(value = "0.0", inclusive = true,
            message = "Price must be positive or zero")
    private BigDecimal price;

    @Size(max = 1000)
    private String description;

    private String coverImage;
}
