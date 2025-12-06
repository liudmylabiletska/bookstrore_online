package mate.academy.bookstoreonline.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface FieldMatch {
    String message() default "Passwords fields must match";

    String[] fields() default {"password", "repeatPassword"};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
