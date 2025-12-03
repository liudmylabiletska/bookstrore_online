package mate.academy.bookstoreonline.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import mate.academy.bookstoreonline.dto.user.UserRegistrationRequestDto;

import java.util.Objects;

public class PasswordValidator implements ConstraintValidator<FieldMatch, UserRegistrationRequestDto> {

    @Override
    public boolean isValid(UserRegistrationRequestDto user,
                           ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(user.getPassword(), user.getRepeatPassword());
    }
}
