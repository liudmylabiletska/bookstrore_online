package mate.academy.bookstoreonline.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mate.academy.bookstoreonline.validator.FieldMatch;

@Getter
@Setter
@FieldMatch(
        fields = {"password", "repeatPassword"},
        message = "Passwords must match")
public class UserRegistrationRequestDto {

    @NotBlank(message = "Email cannot be blank")
    @Size(min = 5, max = 50, message = "Email must be between 5 and 50 characters")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotBlank
    @Size(min = 8, max = 20)
    private String repeatPassword;

    @Size(max = 30, message = "First name cannot exceed 30 characters")
    private String firstName;

    @Size(max = 30, message = "Last name cannot exceed 30 characters")
    private String lastName;

    private String shippingAddress;
}
