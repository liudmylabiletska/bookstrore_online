package mate.academy.bookstoreonline.mapper.orderitem;


import mate.academy.bookstoreonline.dto.orderitem.OrderItemsResponseDto;
import mate.academy.bookstoreonline.model.CartItem;
import mate.academy.bookstoreonline.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(source = "book.price", target = "price")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    OrderItem convertCartItemToOrderItem(CartItem cartItem);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "order.user.id", target = "userId")
    OrderItemsResponseDto toDto(OrderItem orderItem);
}
