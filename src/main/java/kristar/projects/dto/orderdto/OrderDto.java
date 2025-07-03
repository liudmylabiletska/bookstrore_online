package kristar.projects.dto.orderdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(
        Long id,
        Long userId,
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime orderDate,
        String shippingAddress,
        Set<OrderItemResponseDto> orderItems,
        BigDecimal total
) {
}
