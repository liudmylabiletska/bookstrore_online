package kristar.projects.repository.order;

import java.util.List;
import java.util.Optional;
import kristar.projects.model.Order;
import kristar.projects.model.Status;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems", "orderItems.book"})
    List<Order> findAllByUserId(Long userId);

    @EntityGraph(attributePaths = {"orderItems", "orderItems.book"})
    List<Order> findAllByStatus(Status status);

    @EntityGraph(attributePaths = {"orderItems", "orderItems.book"})
    Optional<Order> findByIdAndUserId(Long orderId, Long userId);

}
