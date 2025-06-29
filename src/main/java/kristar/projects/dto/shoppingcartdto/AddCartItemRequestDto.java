package kristar.projects.dto.shoppingcartdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddCartItemRequestDto(
        @NotNull
        @Min(1)
        Long bookId,
        @Min(1)
        int quantity
) {
}
