package kristar.projects.dto.category;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String description;
}
