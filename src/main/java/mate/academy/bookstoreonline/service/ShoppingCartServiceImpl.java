package mate.academy.bookstoreonline.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.dto.cart.CartItemResponseDto;
import mate.academy.bookstoreonline.dto.cart.CartItemUpdateQuantityDto;
import mate.academy.bookstoreonline.dto.cart.ShoppingCartDto;
import mate.academy.bookstoreonline.exception.EntityNotFoundException;
import mate.academy.bookstoreonline.mapper.ShoppingCartMapper;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.model.CartItem;
import mate.academy.bookstoreonline.model.ShoppingCart;
import mate.academy.bookstoreonline.model.User;
import mate.academy.bookstoreonline.repository.BookRepository;
import mate.academy.bookstoreonline.repository.cart.CartItemRepository;
import mate.academy.bookstoreonline.repository.cart.ShoppingCartRepository;
import mate.academy.bookstoreonline.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;

    @Override
    public ShoppingCartDto getCart(Authentication authentication) {
        return shoppingCartMapper.toDto(findCart(authentication));
    }

    @Override
    public ShoppingCartDto save(Authentication authentication, CartItemResponseDto dto) {
        ShoppingCart cart = findCart(authentication);
        Book book = bookRepository.findById(dto.id()).orElseThrow(
                () -> new EntityNotFoundException("No book found with id: " + dto.id())
        );
        CartItem item = new CartItem();
        item.setShoppingCart(cart);
        item.setBook(book);
        item.setQuantity(dto.quantity());
        cart.getCartItems().add(item);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(cart));
    }

    @Override
    public ShoppingCartDto update(Authentication authentication,
                                  Long id, CartItemUpdateQuantityDto dto) {
        ShoppingCart cart = findCart(authentication);
        CartItem cartItem = cartItemRepository
                .findByIdAndShoppingCart(id, findCart(authentication)).orElseThrow(
                        () -> new EntityNotFoundException("Can't find user cart item with id: " + id)
                );
        cartItem.setQuantity(dto.quantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public void delete(Authentication authentication, Long id) {
        cartItemRepository.deleteByIdAndShoppingCart(id, findCart(authentication));
    }

    @Override
    public ShoppingCart createShoppingCart(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        return shoppingCartRepository.save(cart);
    }

    private ShoppingCart findCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartRepository.findByUserId(user.getId());
    }
}
