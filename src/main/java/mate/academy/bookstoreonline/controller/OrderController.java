package mate.academy.bookstoreonline.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.dto.order.CreateOrderRequestDto;
import mate.academy.bookstoreonline.dto.order.OrderResponseDto;
import mate.academy.bookstoreonline.dto.order.UpdateStatusRequestDto;
import mate.academy.bookstoreonline.dto.orderitem.OrderItemsResponseDto;
import mate.academy.bookstoreonline.model.User;
import mate.academy.bookstoreonline.service.order.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Endpoints for managing orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place a new order")
    public OrderResponseDto placeOrder(@AuthenticationPrincipal User user,
                                       @RequestBody @Valid CreateOrderRequestDto orderRequestDto) {
        return orderService.placeOrder(user, orderRequestDto);
    }

    @GetMapping
    @Operation(summary = "Get all orders for the user")
    public List<OrderResponseDto> getAllUserOrders(@AuthenticationPrincipal User user,
                                                       Pageable pageable) {
        return orderService.findAllByUser(user.getId(), pageable);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update order status (Admin only)")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id,
                                                  @RequestBody @Valid UpdateStatusRequestDto updateStatusRequestDto) {
        return orderService.updateStatus(id, updateStatusRequestDto);
    }

    @GetMapping("/{id}/items")
    @Operation(summary = "Get all items for a specific order")
    public Set<OrderItemsResponseDto> getAllOrderItemsInOrder(@PathVariable Long id,
                                                             Pageable pageable) {
        return orderService.findAllOrderItems(id, pageable);
    }

    @GetMapping("/{id}/items/{itemId}")
    @Operation(summary = "Get a specific item from an order")
    public OrderItemsResponseDto getOrderItemInOrder(@PathVariable Long id,
                                                    @PathVariable Long itemId) {
        return orderService.findOrderItemById(id, itemId);
    }
}
