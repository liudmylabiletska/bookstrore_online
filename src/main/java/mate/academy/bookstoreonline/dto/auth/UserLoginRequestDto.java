package mate.academy.bookstoreonline.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
        @NotBlank
        @Size(min = 8, max = 20)
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 20)
        String password
) {
}
