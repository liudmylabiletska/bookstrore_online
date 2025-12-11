package mate.academy.bookstoreonline.dto.auth;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;
import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDto(
        @NotBlank
        @Length(min = 8, max = 20)
        @Email
        String email,

        @NotBlank
        @Length(min = 8, max = 20)
        String password
) {
}
