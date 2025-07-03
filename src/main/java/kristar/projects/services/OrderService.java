package kristar.projects.services;

import java.util.List;
import kristar.projects.dto.orderdto.CreateOrderRequestDto;
import kristar.projects.dto.orderdto.OrderDto;
import kristar.projects.dto.orderdto.OrderItemResponseDto;
import kristar.projects.dto.orderdto.UpdateOrderStatusRequestDto;
import kristar.projects.dto.orderdto.UserOrderResponseDto;
import kristar.projects.model.Status;

public interface OrderService {
    OrderDto save(CreateOrderRequestDto requestDto);

    List<UserOrderResponseDto> findAllOrdersByCurrentUser();

    OrderDto updateOrder(Long orderId, UpdateOrderStatusRequestDto requestDto);

    List<OrderItemResponseDto> findAllItemsByOrderId(Long orderId);

    OrderItemResponseDto findItemByOrderIdItemId(Long orderId, Long itemId);

    List<UserOrderResponseDto> findAllOrdersByStatus(Status status);
}
