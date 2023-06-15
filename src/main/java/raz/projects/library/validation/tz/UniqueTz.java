package raz.projects.library.validation.tz;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueTzValidator.class})
public @interface UniqueTz {

    String fieldName() default "customer";

    String message() default "ID number already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
