package mate.academy.bookstoreonline.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {
    public static final String PATTERN_OF_ISBN = "^(97(8|9))?\\d{9}(\\d|X)$";

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        return isbn != null && Pattern.compile(PATTERN_OF_ISBN).matcher(isbn).matches();
    }

}
