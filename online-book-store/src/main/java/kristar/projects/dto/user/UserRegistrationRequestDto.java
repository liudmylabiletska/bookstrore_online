package kristar.projects.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldMatch(firstField = "password", secondField = "repeatPassword",
        message = "Password and repeat password must match")
public class UserRegistrationRequestDto {
    @NotBlank(message = "Email can not be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password can not be blank")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password;

    @NotBlank(message = "Repeat password can not be blank")
    private String repeatPassword;

    @NotBlank(message = "First name can not be blank")
    private String firstName;

    @NotBlank(message = "Last name can not be blank")
    private String lastName;

    private String shippingAddress;
}
