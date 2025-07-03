package kristar.projects.services;

import java.util.List;
import kristar.projects.dto.order.CreateOrderRequestDto;
import kristar.projects.dto.order.OrderDto;
import kristar.projects.dto.order.OrderItemResponseDto;
import kristar.projects.dto.order.UpdateOrderStatusRequestDto;

public interface OrderService {
    OrderDto save(CreateOrderRequestDto requestDto);

    List<OrderDto> findAllOrdersByCurrentUser();

    OrderDto updateOrder(Long orderId, UpdateOrderStatusRequestDto requestDto);

    List<OrderItemResponseDto> findAllItemsByOrderId(Long orderId);

    OrderItemResponseDto findItemByOrderIdItemId(Long orderId, Long itemId);
}
