package kristar.projects.services;

import kristar.projects.dto.shoppingcart.AddCartItemRequestDto;
import kristar.projects.dto.shoppingcart.ShoppingCartResponseDto;
import kristar.projects.dto.shoppingcart.UpdateCartItemRequestDto;
import kristar.projects.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCartForCurrentUser();

    ShoppingCartResponseDto addBookToCart(AddCartItemRequestDto itemRequestDto);

    ShoppingCartResponseDto updateCartItem(Long cartItemId,
                                           UpdateCartItemRequestDto itemRequestDto);

    void removeCartItem(Long cartItemId);

    void createCartForUser(User user);
}
