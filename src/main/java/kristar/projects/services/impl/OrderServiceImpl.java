package kristar.projects.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kristar.projects.dto.order.CreateOrderRequestDto;
import kristar.projects.dto.order.OrderDto;
import kristar.projects.dto.order.OrderItemResponseDto;
import kristar.projects.dto.order.UpdateOrderStatusRequestDto;
import kristar.projects.exception.EntityNotFoundException;
import kristar.projects.exception.OrderProcessingException;
import kristar.projects.mapper.OrderItemMapper;
import kristar.projects.mapper.OrderMapper;
import kristar.projects.model.Book;
import kristar.projects.model.CartItem;
import kristar.projects.model.Order;
import kristar.projects.model.OrderItem;
import kristar.projects.model.ShoppingCart;
import kristar.projects.model.Status;
import kristar.projects.model.User;
import kristar.projects.repository.order.OrderRepository;
import kristar.projects.repository.orderitem.OrderItemRepository;
import kristar.projects.repository.shoppingcart.ShoppingCartRepository;
import kristar.projects.security.CurrentUserProvider;
import kristar.projects.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    private final CurrentUserProvider currentUserProvider;
    private final ShoppingCartRepository shoppingCartRepository;
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
            throw new OrderProcessingException("Can not place order with empty shoppingCart");
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
    public List<OrderDto> findAllOrdersByCurrentUser() {
        Long userId = currentUserProvider.getCurrentUser().getId();
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream()
                .map((Order order) -> orderMapper.toUserOrderDto(order))
                .toList();
    }

    @Override
    public OrderDto updateOrder(Long orderId, UpdateOrderStatusRequestDto requestDto) {
        Order orderById = orderRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException("Order by ID " + orderId + "not found"));

        orderById.setStatus(requestDto.status());

        return orderMapper.toOrderDto(orderById);
    }

    @Override
    public List<OrderItemResponseDto> findAllItemsByOrderId(Long orderId) {
        User currentUser = currentUserProvider.getCurrentUser();

        Order order = orderRepository.findByIdAndUserId(orderId, currentUser.getId()).orElseThrow(
                () -> new EntityNotFoundException("Order of current user "
                        + "not found with orderId " + orderId)
        );

        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemResponseDto findItemByOrderIdItemId(Long orderId, Long itemId) {
        User currentUser = currentUserProvider.getCurrentUser();

        OrderItem item = orderItemRepository.findByIdAndOrderIdAndUserId(
                itemId,
                orderId,
                currentUser.getId()).orElseThrow(() -> new EntityNotFoundException("Order of"
                        + " current user not found with orderId " + orderId));

        return orderItemMapper.toDto(item);
    }

    @Override
    public List<OrderDto> findAllOrdersByStatus(Status status) {
        List<Order> orders = orderRepository.findAllByStatus(status);
        return orders.stream()
                .map(orderMapper::toUserOrderDto)
                .toList();
    }

    private Order createNewOrder(User currentUser, String shippingAddress) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(currentUser);
        order.setShippingAddress(shippingAddress);
        order.setStatus(Status.pending);
        return order;
    }

    private OrderItem mapCartItemToOrderItem(CartItem cartItem, Order order) {
        OrderItem item = new OrderItem();
        Book book = cartItem.getBook();
        item.setBook(book);
        item.setPrice(book.getPrice());
        item.setOrder(order);
        item.setQuantity(cartItem.getQuantity());
        return item;
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        BigDecimal totalPrice = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.CEILING);

        return totalPrice;
    }
}
