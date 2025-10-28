package mate.academy.bookstoreonline.dto;

import lombok.Data;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotNull
    @Positive
    private BigDecimal price;
    private String description;
    @NotBlank
    private String isbn;
    private String coverImage;
}
