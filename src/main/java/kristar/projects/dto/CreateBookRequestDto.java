package kristar.projects.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    @NotBlank(message = "Title can not be empty")
    @Size(min = 1, max = 300, message = "Title must be between 1 and 300 characters")
    private String title;
    @NotBlank(message = "Author can not be empty")
    @Size(min = 1, max = 100, message = "Author must be between 1 and 100 characters")
    private String author;
    @NotBlank(message = "ISBN cannot be empty")
    @Size(min = 5, max = 20, message = "ISBN must be between 5 and 20 characters")
    private String isbn;
    @NotBlank(message = "Price can not be null")
    @Min(value = 0, message = "Price must be 0 or positive")
    @Digits(integer = 6, fraction = 2, message = "Price must have maximum "
            + "6 digits before decimal and 2 after")
    private BigDecimal price;
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    @Size(max = 500, message = "URL of cover image must be not longer more than 500 characters")
    private String coverImage;
}
