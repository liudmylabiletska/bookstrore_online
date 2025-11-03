package mate.academy.bookstoreonline.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @Positive
    @NotNull
    private BigDecimal price;

    @NotBlank
    private String isbn;

    private String description;

    private String coverImage;
}
