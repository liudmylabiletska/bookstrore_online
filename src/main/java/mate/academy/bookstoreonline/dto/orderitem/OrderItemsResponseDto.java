package mate.academy.bookstoreonline.dto.orderitem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemsResponseDto {
    private Long id;
    private Long bookId;
    private Long userId;
    private int quantity;
}
