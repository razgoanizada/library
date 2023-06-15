package raz.projects.library.validation.tz;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {UniqueTzValidatorIsrael.class})
public @interface UniqueTzIsrael {

    String fieldName() default "customer";
    String message() default "Invalid ID number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
