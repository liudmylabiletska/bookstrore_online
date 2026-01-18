package kristar.projects.dto.order;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequestDto(
        @NotBlank
        String shippingAddress
) {
}
