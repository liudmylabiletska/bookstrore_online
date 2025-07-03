package kristar.projects.dto.orderdto;

public record OrderItemResponseDto(
        Long id,
        Long bookId,
        int quantity
) {
}
