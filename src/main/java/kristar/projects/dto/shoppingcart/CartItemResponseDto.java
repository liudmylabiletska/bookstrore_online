package kristar.projects.dto.shoppingcart;

public record CartItemResponseDto(
        Long id,
        Long bookId,
        String bookTitle,
        int quantity
) {
}
