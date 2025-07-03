package kristar.projects.dto.orderdto;

import jakarta.validation.constraints.NotNull;
import kristar.projects.model.Status;

public record UpdateOrderStatusRequestDto(
        @NotNull
        Status status
) {
}
