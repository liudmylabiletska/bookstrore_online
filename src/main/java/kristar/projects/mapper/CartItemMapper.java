package kristar.projects.mapper;

import kristar.projects.config.MapperConfig;
import kristar.projects.dto.shoppingcartdto.CartItemResponseDto;
import kristar.projects.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toDto(CartItem cartItem);
}
