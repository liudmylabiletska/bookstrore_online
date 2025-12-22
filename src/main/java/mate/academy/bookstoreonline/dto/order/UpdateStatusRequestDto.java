package mate.academy.bookstoreonline.dto.order;

import mate.academy.bookstoreonline.model.enums.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusRequestDto {
    @NotNull
    private Status status;
}
