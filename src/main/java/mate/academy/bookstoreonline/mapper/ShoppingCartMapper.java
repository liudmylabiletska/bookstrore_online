package mate.academy.bookstoreonline.mapper;

import mate.academy.bookstoreonline.dto.cart.ShoppingCartDto;
import mate.academy.bookstoreonline.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import mate.academy.bookstoreonline.config.MapperConfig;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class})
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
