package raz.projects.library.validation.tz;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UniqueTzValidatorIsrael implements ConstraintValidator<UniqueTzIsrael, String> {

    @Override
    public boolean isValid(String tz, ConstraintValidatorContext context) {


        if (tz == null) {
            return false;
        }

        if (tz.length() != 9) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(tz.charAt(i));

            if (i % 2 == 0) {
                digit *= 1;
            } else {
                digit *= 2;
                if (digit > 9) {
                    digit = digit / 10 + digit % 10;
                }
            }

            sum += digit;
        }

        return sum % 10 == 0;
    }
}
