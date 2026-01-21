package mate.academy.bookstoreonline.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstoreonline.dto.cart.CartItemRequestDto;
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
import mate.academy.bookstoreonline.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private static final String USER_EMAIL = "bileckaludmila74@gmail.com";

    @Test
    @DisplayName("Verify getCart returns correct DTO based on User Email")
    void getCart_ValidUser_ReturnsDto() {
        User user = setupUserAndAuth(authentication);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(10L);
        cart.setUser(user);
        ShoppingCartDto expectedDto = new ShoppingCartDto(10L, 1L, Set.of());
        when(shoppingCartRepository.findByUserEmail(USER_EMAIL)).thenReturn(Optional.of(cart));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);

        ShoppingCartDto actualDto = shoppingCartService.getCart(authentication);

        assertNotNull(actualDto);
        assertEquals(1L, actualDto.userId());
    }

    @Test
    @DisplayName("Successfully add a new book to the shopping cart")
    void addBookToShoppingCart_ValidRequest_ReturnsDto() {
        User user = setupUserAndAuth(authentication);
        CartItemRequestDto requestDto = new CartItemRequestDto(10L, 2);
        Book book = new Book();
        book.setId(10L);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());
        ShoppingCartDto expectedDto = new ShoppingCartDto(1L, 1L, Collections.emptySet());

        when(shoppingCartRepository.findByUserEmail(USER_EMAIL)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);

        ShoppingCartDto result = shoppingCartService.save(authentication, requestDto);

        assertNotNull(result);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    @DisplayName("Successfully remove an item from the shopping cart")
    void delete_ValidId_ReturnsUpdatedCartDto() {
        setupUserAndAuth(authentication);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        CartItem cartItem = new CartItem();
        cartItem.setId(5L);
        cartItem.setShoppingCart(cart);

        when(shoppingCartRepository.findByUserEmail(USER_EMAIL)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndShoppingCartId(5L, 1L)).thenReturn(Optional.of(cartItem));

        shoppingCartService.delete(authentication, 5L);

        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    @DisplayName("Successfully update cart item quantity")
    void update_ValidRequest_ReturnsUpdatedDto() {
        setupUserAndAuth(authentication);
        CartItemUpdateQuantityDto updateDto = new CartItemUpdateQuantityDto(5);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(2);
        cartItem.setShoppingCart(cart);
        ShoppingCartDto expectedDto = new ShoppingCartDto(1L, 1L, Collections.emptySet());

        when(shoppingCartRepository.findByUserEmail(USER_EMAIL)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndShoppingCartId(1L, 1L)).thenReturn(Optional.of(cartItem));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);

        shoppingCartService.update(authentication, 1L, updateDto);

        assertEquals(5, cartItem.getQuantity());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    @DisplayName("Update quantity - Should throw Exception when item ID is invalid")
    void update_InvalidItemId_ThrowsException() {
        setupUserAndAuth(authentication);
        CartItemUpdateQuantityDto updateDto = new CartItemUpdateQuantityDto(5);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);

        when(shoppingCartRepository.findByUserEmail(USER_EMAIL)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndShoppingCartId(999L, 1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                shoppingCartService.update(authentication, 999L, updateDto)
        );
    }

    @Test
    @DisplayName("Add book - Should throw EntityNotFoundException when book does not exist")
    void save_InvalidBookId_ThrowsException() {
        setupUserAndAuth(authentication);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setCartItems(new HashSet<>());
        CartItemRequestDto requestDto = new CartItemRequestDto(100L, 2);

        when(shoppingCartRepository.findByUserEmail(USER_EMAIL)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                shoppingCartService.save(authentication, requestDto)
        );
    }

    private User setupUserAndAuth(Authentication auth) {
        User user = new User();
        user.setId(1L);
        user.setEmail(USER_EMAIL);
        when(auth.getName()).thenReturn(USER_EMAIL);
        when(auth.getPrincipal()).thenReturn(user);

        return user;
    }
}
