package mate.academy.bookstoreonline.dto.order;

import mate.academy.bookstoreonline.model.OrderItem;
import java.math.BigDecimal;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemDto {
    private Set<OrderItem> orderItems;
    private BigDecimal totalPrice;
}
