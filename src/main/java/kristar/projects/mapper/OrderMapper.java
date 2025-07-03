package kristar.projects.mapper;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kristar.projects.config.MapperConfig;
import kristar.projects.dto.orderdto.OrderDto;
import kristar.projects.dto.orderdto.OrderItemResponseDto;
import kristar.projects.dto.orderdto.UserOrderResponseDto;
import kristar.projects.model.Book;
import kristar.projects.model.Order;
import kristar.projects.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderDto toOrderDto(Order order);

    @Mapping(source = "user.id", target = "userId")
    UserOrderResponseDto toUserOrderDto(Order order);

    default List<OrderItemResponseDto> toDtoList(Set<OrderItem> orderItems,
                                                 OrderItemMapper mapper
    ) {
        return orderItems.stream()
                .filter(item -> item.getQuantity() > 0)
                .sorted(Comparator.comparing(item -> item.getBook().getTitle()))
                .map((OrderItem orderItem) -> mapper.toDto(orderItem))
                .toList();
    }

    default Set<OrderItem> fromDtoList(List<OrderItemResponseDto> dtoList,
                                       Order order
    ) {
        return dtoList.stream()
                .map(dto -> {
                    OrderItem item = new OrderItem();
                    Book book = new Book();
                    book.setId(dto.bookId());
                    item.setBook(book);
                    item.setQuantity(dto.quantity());
                    item.setOrder(order);
                    return item;
                })
                .collect(Collectors.toSet());
    }
}
