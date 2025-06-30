package kristar.projects.repository.cartitem;

import java.util.Optional;
import kristar.projects.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("""
            SELECT ci FROM CartItem ci
            JOIN FETCH ci.book
            WHERE ci.shoppingCart.id = :shoppingCartId AND ci.book.id = :bookId
            """)
    Optional<CartItem> findByCartIdAndBookId(@Param("shoppingCartId") Long shoppingCartId,
                                             @Param("bookId") Long bookId);
}
