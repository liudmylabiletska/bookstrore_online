package mate.academy.bookstoreonline.dto.cart;

import jakarta.validation.constraints.Positive;

public record CartItemUpdateQuantityDto(
        @Positive
        int quantity
) {}
