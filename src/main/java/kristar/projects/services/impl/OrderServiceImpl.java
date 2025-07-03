package kristar.projects.services.impl;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kristar.projects.dto.orderdto.CreateOrderRequestDto;
import kristar.projects.dto.orderdto.OrderDto;
import kristar.projects.dto.orderdto.OrderItemResponseDto;
import kristar.projects.dto.orderdto.UpdateOrderStatusRequestDto;
import kristar.projects.dto.orderdto.UserOrderResponseDto;
import kristar.projects.exception.EntityNotFoundException;
import kristar.projects.mapper.OrderItemMapper;
import kristar.projects.mapper.OrderMapper;
import kristar.projects.model.CartItem;
import kristar.projects.model.Order;
import kristar.projects.model.OrderItem;
import kristar.projects.model.ShoppingCart;
import kristar.projects.model.Status;
import kristar.projects.model.User;
import kristar.projects.model.builder.OrderItemBuilder;
import kristar.projects.repository.order.OrderRepository;
import kristar.projects.repository.orderitem.OrderItemRepository;
import kristar.projects.repository.shoppingcart.ShoppingCartRepository;
import kristar.projects.security.CurrentUserProvider;
import kristar.projects.services.OrderService;
import kristar.projects.services.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    private final CurrentUserProvider currentUserProvider;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderDto save(CreateOrderRequestDto requestDto) {
        User currentUser = currentUserProvider.getCurrentUser();

        ShoppingCart shoppingCart = shoppingCartRepository
                .findWithItemsByUserId(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can not found "
                        + "shoppingCart by current User with id " + currentUser.getId()));

        if (shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalArgumentException("Can not place order with empty shoppingCart");
        }

        Order order = createNewOrder(currentUser, requestDto.shippingAddress());

        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(item -> mapCartItemToOrderItem(item, order))
                .collect(Collectors.toSet());

        order.setOrderItems(orderItems);
        order.setTotal(calculateTotal(orderItems));

        orderRepository.save(order);

        return orderMapper.toOrderDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOrderResponseDto> findAllOrdersByCurrentUser() {
        Long userId = currentUserProvider.getCurrentUser().getId();
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream()
                .map((Order order) -> orderMapper.toUserOrderDto(order))
                .toList();
    }

    @Override
    public OrderDto updateOrder(Long orderId, UpdateOrderStatusRequestDto requestDto) {
        Order orderById = getOrderById(orderId);
        orderById.setStatus(requestDto.status());

        return orderMapper.toOrderDto(getOrderById(orderId));
    }

    @Override
    public List<OrderItemResponseDto> findAllItemsByOrderId(Long orderId) {
        orderIsValid(getOrderById(orderId));

        return getOrderById(orderId).getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponseDto findItemByOrderIdItemId(Long orderId, Long itemId) {
        Order orderById = getOrderById(orderId);
        orderIsValid(orderById);

        return orderById.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .map(item -> orderItemMapper.toDto(item))
                .orElseThrow(() -> new EntityNotFoundException("OrderItem with id "
                        + itemId + "not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOrderResponseDto> findAllOrdersByStatus(Status status) {
        List<Order> orders = orderRepository.findAllByStatus(status);
        return orders.stream()
                .map(orderMapper::toUserOrderDto)
                .toList();
    }

    private Order createNewOrder(User currentUser, @NotBlank String shippingAddress) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(currentUser);
        order.setShippingAddress(shippingAddress);
        order.setStatus(Status.PENDING);
        return order;
    }

    private OrderItem mapCartItemToOrderItem(CartItem cartItem, Order order) {
        return new OrderItemBuilder()
                .forOrder(order)
                .withBook(cartItem.getBook())
                .withQuantity(cartItem.getQuantity())
                .build();
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        BigDecimal totalPrice = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.CEILING);

        return totalPrice;
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException("Order by ID " + orderId + "not found"));
    }

    private void orderIsValid(Order orderById) {
        if (!orderById.getUser().getId().equals(currentUserProvider.getCurrentUser().getId())) {
            throw new AccessDeniedException("Access error: not your "
                    + "order with id " + orderById.getId());
        }
    }
}
