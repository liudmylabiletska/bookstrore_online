package kristar.projects.dto.shoppingcartdto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddCartItemRequestDto(
        @Positive
        @NotNull
        Long bookId,
        @Positive
        @NotNull
        int quantity
) {
}
