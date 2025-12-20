package mate.academy.bookstoreonline.repository.cart;

import mate.academy.bookstoreonline.model.ShoppingCart;
import mate.academy.bookstoreonline.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findByUserId(Long userId);
}
