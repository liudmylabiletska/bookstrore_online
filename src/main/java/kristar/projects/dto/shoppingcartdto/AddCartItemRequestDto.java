package kristar.projects.dto.shoppingcartdto;

import jakarta.validation.constraints.Positive;

public record AddCartItemRequestDto(
        @Positive
        Long bookId,
        @Positive
        int quantity
) {
}
