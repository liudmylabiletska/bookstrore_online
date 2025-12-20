package mate.academy.bookstoreonline.service;

import mate.academy.bookstoreonline.dto.cart.CartItemResponseDto;
import mate.academy.bookstoreonline.dto.cart.CartItemUpdateQuantityDto;
import mate.academy.bookstoreonline.dto.cart.ShoppingCartDto;
import mate.academy.bookstoreonline.model.ShoppingCart;
import mate.academy.bookstoreonline.model.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto getCart(Authentication authentication);

    ShoppingCartDto save(Authentication authentication, CartItemResponseDto dto);

    ShoppingCartDto update(Authentication authentication,
                           Long id, CartItemUpdateQuantityDto dto);

    void delete(Authentication authentication, Long id);

    ShoppingCart createShoppingCart(User user);
}
