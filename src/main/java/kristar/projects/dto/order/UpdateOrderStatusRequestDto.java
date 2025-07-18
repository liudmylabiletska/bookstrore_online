package kristar.projects.dto.order;

import jakarta.validation.constraints.NotNull;
import kristar.projects.model.StatusName;

public record UpdateOrderStatusRequestDto(
        @NotNull
        StatusName status
) {
}
