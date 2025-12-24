package mate.academy.bookstoreonline.service.order;

import java.util.List;
import java.util.Set;
import mate.academy.bookstoreonline.dto.order.CreateOrderRequestDto;
import mate.academy.bookstoreonline.dto.order.OrderResponseDto;
import mate.academy.bookstoreonline.dto.order.UpdateStatusRequestDto;
import mate.academy.bookstoreonline.dto.orderitem.OrderItemsResponseDto;
import mate.academy.bookstoreonline.model.User;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto placeOrder(User user, CreateOrderRequestDto requestDto);

    List<OrderResponseDto> findAllByUser(Long userId, Pageable pageable);

    OrderResponseDto updateStatus(Long id, UpdateStatusRequestDto statusDto);

    Set<OrderItemsResponseDto> findAllOrderItems(Long orderId, Pageable pageable);

    OrderItemsResponseDto findOrderItemById(Long orderId, Long itemId);
}
