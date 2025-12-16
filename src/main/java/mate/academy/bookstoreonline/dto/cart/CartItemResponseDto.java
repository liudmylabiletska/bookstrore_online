package mate.academy.bookstoreonline.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemResponseDto(
        @NotNull
        @Positive
        Long id,
        @Positive
        int quantity
) {

}
