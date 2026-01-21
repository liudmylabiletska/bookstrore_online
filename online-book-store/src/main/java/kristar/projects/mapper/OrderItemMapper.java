package kristar.projects.mapper;

import kristar.projects.config.MapperConfig;
import kristar.projects.dto.order.OrderItemResponseDto;
import kristar.projects.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);
}
