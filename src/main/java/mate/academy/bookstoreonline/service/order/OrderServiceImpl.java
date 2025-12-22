package mate.academy.bookstoreonline.service.order;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.dto.order.CreateOrderRequestDto;
import mate.academy.bookstoreonline.dto.order.UpdateStatusRequestDto;
import mate.academy.bookstoreonline.dto.order.OrderResponseDto;
import mate.academy.bookstoreonline.dto.orderitem.OrderItemsResponseDto;
import mate.academy.bookstoreonline.mapper.order.OrderMapper;
import mate.academy.bookstoreonline.mapper.orderitem.OrderItemMapper;
import mate.academy.bookstoreonline.model.Order;
import mate.academy.bookstoreonline.model.OrderItem;
import mate.academy.bookstoreonline.model.ShoppingCart;
import mate.academy.bookstoreonline.model.User;
import mate.academy.bookstoreonline.model.enums.Status;
import mate.academy.bookstoreonline.repository.cart.ShoppingCartRepository;
import org.springframework.data.domain.Pageable;
import mate.academy.bookstoreonline.repository.order.OrderRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderResponseDto placeOrder(User user, CreateOrderRequestDto requestDto) {
        ShoppingCart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Cart not found for user: " + user.getId()));

        Order order = new Order();
        order.setUser(user);
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);

        Set<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem item = new OrderItem();
                    item.setBook(cartItem.getBook());
                    item.setQuantity(cartItem.getQuantity());
                    item.setPrice(cartItem.getBook().getPrice());
                    item.setOrder(order);
                    return item;
                })
                .collect(Collectors.toSet());

        order.setOrderItems(orderItems);
        order.setTotal(calculateTotal(orderItems));

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> findAllByUser(Long userId, org.springframework.data.domain.Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponseDto updateStatus(Long id, UpdateStatusRequestDto statusDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order by id: " + id));
        order.setStatus(statusDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Set<OrderItemsResponseDto> findAllOrderItems(Long orderId, Pageable pageable) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order by id: " + orderId));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemsResponseDto findOrderItemById(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order by id: " + orderId));
        return order.getOrderItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .map(orderItemMapper::toDto)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Can't find item by id: " + itemId));
    }

    private BigDecimal calculateTotal(Set<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
