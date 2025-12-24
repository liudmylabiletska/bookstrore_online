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

import java.util.Optional;

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
    @Transactional
    public ShoppingCartDto save(Authentication authentication, CartItemResponseDto requestDto) {
        ShoppingCart cart = findCart(authentication);
        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(requestDto.bookId()))
                .findFirst();
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + requestDto.quantity());
        } else {
            Book book = bookRepository.findById(requestDto.bookId())
                    .orElseThrow(() -> new EntityNotFoundException("Can't find book by id: " + requestDto.bookId()));

            CartItem newItem = new CartItem();
            newItem.setShoppingCart(cart);
            newItem.setBook(book);
            newItem.setQuantity(requestDto.quantity());

            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        }
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    public ShoppingCartDto update(Authentication authentication, Long id, CartItemUpdateQuantityDto dto) {
        ShoppingCart cart = findCart(authentication);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find user cart item with id: " + id));

        cartItem.setQuantity(dto.quantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public void delete(Authentication authentication, Long cartItemId) {
        ShoppingCart cart = findCart(authentication);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(cartItemId, cart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item..."));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public ShoppingCart createShoppingCart(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        return shoppingCartRepository.save(cart);
    }

    private ShoppingCart findCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find shopping cart for user id: " + user.getId()));
    }
}
