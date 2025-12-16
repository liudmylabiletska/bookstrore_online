package mate.academy.bookstoreonline.repository.cart;

import java.util.Optional;
import mate.academy.bookstoreonline.model.CartItem;
import mate.academy.bookstoreonline.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByIdAndShoppingCart(Long id, ShoppingCart cart);

    void deleteByIdAndShoppingCart(Long id, ShoppingCart cart);
}
