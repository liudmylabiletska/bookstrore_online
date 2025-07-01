package kristar.projects.services.impl;

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
import kristar.projects.security.CurrentUserProvider;
import kristar.projects.services.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CurrentUserProvider currentUserProvider;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponseDto getCartForCurrentUser() {
        User currentUser = currentUserProvider.getCurrentUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findWithItemsByUserId(
                currentUser.getId()).orElseGet(() -> createCartForUser(currentUser));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto addBookToCart(AddCartItemRequestDto itemRequestDto) {
        User currentUser = currentUserProvider.getCurrentUser();

        ShoppingCart shoppingCart = shoppingCartRepository
                .findWithItemsByUserId(currentUser.getId())
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
        User currentUser = currentUserProvider.getCurrentUser();
        ShoppingCart shoppingCart = shoppingCartRepository
                .findWithItemsByUserId(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can not found "
                        + "shoppingCart by current User"));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(
                        cartItemId,
                        shoppingCart.getId()
                )
                .orElseThrow(() -> new EntityNotFoundException(("CartItem with id %d not found in"
                        + " cart %d").formatted(cartItemId, shoppingCart.getId())));
        cartItem.setQuantity(itemRequestDto.quantity());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        User currentUser = currentUserProvider.getCurrentUser();
        ShoppingCart shoppingCart = shoppingCartRepository
                .findWithItemsByUserId(currentUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can not found "
                        + "shoppingCart by current User"));
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(
                        cartItemId,
                        shoppingCart.getId()
                )
                .orElseThrow(() -> new EntityNotFoundException(("CartItem with id %d not found"
                        + " in cart %d").formatted(cartItemId, shoppingCart.getId())));
        shoppingCart.getCartItems().remove(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart createCartForUser(User user) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUser(user);
        return shoppingCartRepository.save(cart);
    }

    private CartItem createCartItem(ShoppingCart shoppingCart, Book book) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(1);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return cartItem;
    }
}
