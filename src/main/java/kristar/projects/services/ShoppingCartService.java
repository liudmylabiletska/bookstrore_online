package kristar.projects.services;

import kristar.projects.dto.shoppingcartdto.AddCartItemRequestDto;
import kristar.projects.dto.shoppingcartdto.ShoppingCartResponseDto;
import kristar.projects.dto.shoppingcartdto.UpdateCartItemRequestDto;
import kristar.projects.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCartForCurrentUser();

    ShoppingCartResponseDto addBookToCart(AddCartItemRequestDto itemRequestDto);

    ShoppingCartResponseDto updateCartItem(Long cartItemId,
                                           UpdateCartItemRequestDto itemRequestDto);

    void removeCartItem(Long cartItemId);

    void createCartForUser(User user);
}
