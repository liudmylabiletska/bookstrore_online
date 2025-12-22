package mate.academy.bookstoreonline.repository.orderitem;

import java.util.Optional;
import mate.academy.bookstoreonline.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT с FROM OrderItem с WHERE с.order.id = :orderId AND с.id = :itemId")
    Optional<OrderItem> findOrderItemByIdInOrderById(@Param("orderId") Long orderId,
                                                     @Param("itemId") Long itemId);
}
