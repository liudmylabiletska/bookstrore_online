package kristar.projects.dto.shoppingcartdto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemRequestDto(
        @Positive
        @NotNull
        int quantity
) {
}
