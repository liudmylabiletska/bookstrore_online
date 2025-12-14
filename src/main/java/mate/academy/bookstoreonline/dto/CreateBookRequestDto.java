package mate.academy.bookstoreonline.dto;

import java.util.List;
import java.math.BigDecimal;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

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

    @NotEmpty
    private List<Long> categoryIds;
}
