package kristar.projects.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class UpdateBookRequestDto {
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotBlank(message = "Author cannot be blank")
    private String author;
    @NotBlank(message = "ISBN cannot be blank")
    private String isbn;
    @NotNull(message = "Price can not be null")
    @Min(value = 0, message = "Price must be 0 or positive")
    @Digits(integer = 6, fraction = 2, message = "Price must have maximum "
            + "6 digits before decimal and 2 after")
    private BigDecimal price;
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;
    @Size(max = 500, message = "URL of cover image must be not longer more than 500 characters")
    private String coverImage;
}
