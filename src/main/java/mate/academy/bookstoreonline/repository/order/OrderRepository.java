package mate.academy.bookstoreonline.repository.order;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstoreonline.model.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Set<Order>> findAllOrdersByUserId(Long userId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.id = :orderId")
    List<Order> findAllByUserId(Long userId, Pageable pageable);
}
