package kristar.projects.mapper;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kristar.projects.config.MapperConfig;
import kristar.projects.dto.shoppingcartdto.CartItemResponseDto;
import kristar.projects.dto.shoppingcartdto.ShoppingCartResponseDto;
import kristar.projects.model.Book;
import kristar.projects.model.CartItem;
import kristar.projects.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = ShoppingCartMapper.class)
public interface ShoppingCartMapper {
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toDto(CartItem cartItem);

    @Mapping(source = "user.id", target = "userId")
    ShoppingCartResponseDto toDto(ShoppingCart cart);

    default List<CartItemResponseDto> toDtoList(Set<CartItem> cartItems) {
        return cartItems.stream()
                .filter(item -> item.getQuantity() > 0)
                .sorted(Comparator.comparing(item -> item.getBook().getTitle()))
                .map(this::toDto)
                .toList();
    }

    default Set<CartItem> fromDtoList(List<CartItemResponseDto> dtoList,
                                      ShoppingCart shoppingCart
    ) {
        return dtoList.stream()
                .map(dto -> {
                    CartItem item = new CartItem();
                    Book book = new Book();
                    book.setId(dto.bookId());
                    item.setBook(book);
                    item.setQuantity(dto.quantity());
                    item.setShoppingCart(shoppingCart);
                    return item;
                })
                .collect(Collectors.toSet());
    }
}
