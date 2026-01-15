package mate.academy.bookstoreonline.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank(message = "Name is required")
    private String name;

    @Size(max = 1000)
    private String description;
}
