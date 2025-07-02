package kristar.projects.dto.shoppingcartdto;

import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequestDto(
        @Positive
        int quantity
) {
}
