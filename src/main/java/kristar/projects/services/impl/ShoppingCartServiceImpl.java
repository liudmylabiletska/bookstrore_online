package kristar.projects.services.impl;

import java.nio.file.AccessDeniedException;
import kristar.projects.dto.shoppingcartdto.AddCartItemRequestDto;
import kristar.projects.dto.shoppingcartdto.ShoppingCartResponseDto;
import kristar.projects.dto.shoppingcartdto.UpdateCartItemRequestDto;
import kristar.projects.exception.EntityNotFoundException;
import kristar.projects.mapper.ShoppingCartMapper;
import kristar.projects.model.Book;
import kristar.projects.model.CartItem;
import kristar.projects.model.ShoppingCart;
import kristar.projects.model.User;
import kristar.projects.repository.book.BookRepository;
import kristar.projects.repository.cartitem.CartItemRepository;
import kristar.projects.repository.shoppingcart.ShoppingCartRepository;
import kristar.projects.services.ShoppingCartService;
import kristar.projects.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto getCartForCurrentUser() throws AccessDeniedException {
        User currenUser = userService.getCurrentUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findWithItemsByUserId(
                currenUser.getId()).orElseGet(() -> {
                    ShoppingCart newShoppingCart = new ShoppingCart();
                    newShoppingCart.setUser(currenUser);
                    return shoppingCartRepository.save(newShoppingCart);
                });
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto addBookToCart(AddCartItemRequestDto itemRequestDto)
            throws AccessDeniedException {
        User currenUser = userService.getCurrentUser();

        ShoppingCart shoppingCart = shoppingCartRepository
                .findWithItemsByUserId(currenUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can not found "
                        + "shoppingCart by current User"));

        Book book = bookRepository.findById(itemRequestDto.bookId())
                .orElseThrow(() -> new EntityNotFoundException("Can not found Book with"
                        + " requested id " + itemRequestDto.bookId()));

        CartItem cartItem = cartItemRepository.findByCartIdAndBookId(
                shoppingCart.getId(),
                book.getId()).orElseGet(() -> createCartItem(shoppingCart, book));
        cartItem.setQuantity(cartItem.getQuantity() + itemRequestDto.quantity());

        shoppingCartRepository.save(shoppingCart);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto updateCartItem(Long cartItemId,
                                                  UpdateCartItemRequestDto itemRequestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can not found cartItem with id " + cartItemId));
        cartItem.setQuantity(itemRequestDto.quantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can not found cartItem with id " + cartItemId));
        ShoppingCart shoppingCart = cartItem.getShoppingCart();
        shoppingCart.getCartItems().remove(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }

    private CartItem createCartItem(ShoppingCart shoppingCart, Book book) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(1);
        shoppingCart.getCartItems().add(cartItem);
        return cartItem;
    }
}
