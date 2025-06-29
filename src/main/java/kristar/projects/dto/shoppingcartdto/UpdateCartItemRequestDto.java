package kristar.projects.dto.shoppingcartdto;

import jakarta.validation.constraints.Min;

public record UpdateCartItemRequestDto(
        @Min(1)
        int quantity
) {
}
