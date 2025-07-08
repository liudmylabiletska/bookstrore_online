package kristar.projects.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import kristar.projects.model.StatusName;

public record OrderDto(
        Long id,
        Long userId,
        Set<OrderItemResponseDto> orderItems,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime orderDate,
        BigDecimal total,
        StatusName status
) {
}
