package kristar.projects.repository.orderitem;

import java.util.Optional;
import kristar.projects.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("""
    SELECT oi FROM OrderItem oi
    JOIN FETCH oi.book
    JOIN oi.order o
    JOIN o.user u
    WHERE oi.id = :itemId
      AND o.id = :orderId
      AND u.id = :userId
            """)
    Optional<OrderItem> findByIdAndOrderIdAndUserId(
            @Param("itemId") Long itemId,
            @Param("orderId") Long orderId,
            @Param("userId") Long userId
    );
}
