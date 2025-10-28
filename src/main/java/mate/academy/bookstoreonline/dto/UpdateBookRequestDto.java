package mate.academy.bookstoreonline.dto;

import java.math.BigDecimal;
import lombok.Data;
import jakarta.validation.constraints.NotBlank; 
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

@Data
public class UpdateBookRequestDto {
    @NotBlank(message = "Title is required and must not be blank.")
    @Size(max = 255, message = "Title cannot exceed 255 characters")
    private String title;

    @Size(max = 255, message = "Author name cannot exceed 255 characters")
    private String author;

    @Size(min = 10, max = 17, message = "ISBN must be between 10 and 17 characters.")
    private String isbn;

    @NotNull(message = "Price must not be null.")
    @PositiveOrZero(message = "Price must be positive or zero.")
    private BigDecimal price;

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    private String description;

    @Size(max = 255, message = "Cover image URL cannot exceed 255 characters")
    private String coverImage;
}
