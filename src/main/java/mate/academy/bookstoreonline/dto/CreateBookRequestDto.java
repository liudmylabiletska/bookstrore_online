package mate.academy.bookstoreonline.dto;

import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size; 

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Size(max = 255, message = "Author name cannot exceed 255 characters")
    private String author;

    @NotNull(message = "Price is required.")
    @PositiveOrZero(message = "Price must be positive or zero.")
    private BigDecimal price;

    @NotBlank(message = "ISBN is required.")
    @Size(min = 10, max = 17, message = "ISBN must be between 10 and 17 characters.")
    private String isbn;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Size(max = 255, message = "Cover image URL cannot exceed 255 characters")
    private String coverImage;
}
