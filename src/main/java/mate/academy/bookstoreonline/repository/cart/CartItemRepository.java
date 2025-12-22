package mate.academy.bookstoreonline.repository.cart;

import java.util.Optional;
import java.util.Set;
import mate.academy.bookstoreonline.model.CartItem;
import mate.academy.bookstoreonline.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCartId(Long id, Long shoppingCartId);

    void deleteByIdAndShoppingCartId(Long id, Long shoppingCartId);

    Optional<Set<CartItem>> getAllCartItemsByShoppingCartId(Long shoppingCartId);
}
