package kristar.projects.dto.order;

import jakarta.validation.constraints.NotNull;
import kristar.projects.model.Status;

public record UpdateOrderStatusRequestDto(
        @NotNull
        Status status
) {
}
