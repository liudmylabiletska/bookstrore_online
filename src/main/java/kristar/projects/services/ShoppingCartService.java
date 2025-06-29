package kristar.projects.services;

import java.nio.file.AccessDeniedException;
import kristar.projects.dto.shoppingcartdto.AddCartItemRequestDto;
import kristar.projects.dto.shoppingcartdto.ShoppingCartResponseDto;
import kristar.projects.dto.shoppingcartdto.UpdateCartItemRequestDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCartForCurrentUser() throws AccessDeniedException;

    ShoppingCartResponseDto addBookToCart(AddCartItemRequestDto itemRequestDto)
            throws AccessDeniedException;

    ShoppingCartResponseDto updateCartItem(Long cartItemId,
                                           UpdateCartItemRequestDto itemRequestDto);

    void removeCartItem(Long cartItemId);
}
