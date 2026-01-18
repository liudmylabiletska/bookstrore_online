package mate.academy.bookstoreonline.service;

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
import mate.academy.bookstoreonline.service.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceImplTest {

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private User setupUserAndAuth(Authentication auth) {
        User user = new User();
        user.setId(1L);
        user.setEmail("bileckaludmila74@gmail.com");
        when(auth.getPrincipal()).thenReturn(user);
        return user;
    }

    @Test
    @DisplayName("Verify getCart returns correct DTO based on User ID")
    void getCart_ValidUser_ReturnsDto() {
        Authentication authentication = mock(Authentication.class);
        User user = setupUserAndAuth(authentication);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(10L);
        cart.setUser(user);
        ShoppingCartDto expectedDto = new ShoppingCartDto(10L, 1L, Set.of());
        when(shoppingCartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);
        ShoppingCartDto actualDto = shoppingCartService.getCart(authentication);
        assertNotNull(actualDto);
        assertEquals(1L, actualDto.userId());
    }

    @Test
    @DisplayName("Successfully add a new book to the shopping cart")
    void addBookToShoppingCart_ValidRequest_ReturnsDto() {
        Authentication auth = mock(Authentication.class);
        User user = setupUserAndAuth(auth);
        CartItemResponseDto requestDto = new CartItemResponseDto(10L, 2);
        Book book = new Book();
        book.setId(10L);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());
        ShoppingCartDto expectedDto = new ShoppingCartDto(1L, 1L, Collections.emptySet());
        when(shoppingCartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);
        ShoppingCartDto result = shoppingCartService.save(auth, requestDto);
        assertNotNull(result);
        verify(cartItemRepository).save(any(CartItem.class));
        verify(bookRepository).findById(10L);
    }

    @Test
    @DisplayName("Successfully remove an item from the shopping cart")
    void delete_ValidId_ReturnsUpdatedCartDto() {
        Authentication auth = mock(Authentication.class);
        User user = setupUserAndAuth(auth);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setUser(user);
        CartItem cartItem = new CartItem();
        cartItem.setId(5L);
        cartItem.setShoppingCart(cart);
        when(shoppingCartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndShoppingCartId(5L, 1L)).thenReturn(Optional.of(cartItem));
        shoppingCartService.delete(auth, 5L);
        verify(cartItemRepository).delete(cartItem);
    }

    @Test
    @DisplayName("Successfully update cart item quantity")
    void update_ValidRequest_ReturnsUpdatedDto() {
        Authentication auth = mock(Authentication.class);
        User user = setupUserAndAuth(auth);
        CartItemUpdateQuantityDto updateDto = new CartItemUpdateQuantityDto(5);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setUser(user);
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setQuantity(2);
        cartItem.setShoppingCart(cart);
        ShoppingCartDto expectedDto = new ShoppingCartDto(1L, 1L, Collections.emptySet());
        when(shoppingCartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndShoppingCartId(1L, 1L)).thenReturn(Optional.of(cartItem));
        when(shoppingCartMapper.toDto(cart)).thenReturn(expectedDto);
        ShoppingCartDto result = shoppingCartService.update(auth, 1L, updateDto);
        assertEquals(5, cartItem.getQuantity());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    @DisplayName("Update quantity - Should throw Exception when item ID is invalid")
    void update_InvalidItemId_ThrowsException() {
        Authentication auth = mock(Authentication.class);
        User user = setupUserAndAuth(auth);
        CartItemUpdateQuantityDto updateDto = new CartItemUpdateQuantityDto(5);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        when(shoppingCartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByIdAndShoppingCartId(999L, 1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                shoppingCartService.update(auth, 999L, updateDto)
        );
    }

    @Test
    @DisplayName("Додавання книги - має кинути EntityNotFoundException, якщо книга не знайдена")
    void save_InvalidBookId_ThrowsException() {
        Authentication auth = mock(Authentication.class);
        User user = setupUserAndAuth(auth);
        ShoppingCart cart = new ShoppingCart();
        cart.setId(1L);
        cart.setCartItems(new HashSet<>());
        CartItemResponseDto requestDto = new CartItemResponseDto(100L, 2);
        when(shoppingCartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () ->
                shoppingCartService.save(auth, requestDto)
        );
    }
}
