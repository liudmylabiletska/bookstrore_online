package kristar.projects.dto.categorydto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryRequestDto {
    @NotBlank(message = "Name of category can not be empty")
    @Size(min = 1, max = 300, message = "Category must be between 1 and 300 characters")
    private String name;

    @Size(max = 1000, message = "Description of category cannot exceed 1000 characters")
    private String description;
}
