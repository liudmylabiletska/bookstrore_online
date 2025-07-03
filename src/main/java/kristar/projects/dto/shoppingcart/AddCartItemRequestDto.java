package kristar.projects.dto.shoppingcart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AddCartItemRequestDto(
        @Positive
        @NotNull
        Long bookId,
        @Positive
        int quantity
) {
}
