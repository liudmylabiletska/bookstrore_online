package mate.academy.bookstoreonline.mapper.order;

import mate.academy.bookstoreonline.dto.order.OrderResponseDto;
import mate.academy.bookstoreonline.mapper.orderitem.OrderItemMapper;
import mate.academy.bookstoreonline.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toDto(Order order);
}
