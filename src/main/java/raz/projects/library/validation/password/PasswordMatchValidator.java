package raz.projects.library.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import raz.projects.library.dto.update.LibrarianChangePassword;

@RequiredArgsConstructor
public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, LibrarianChangePassword> {


    @Override
    public boolean isValid(LibrarianChangePassword dto, ConstraintValidatorContext context) {
        return dto.getNewPassword().equals(dto.getRepeatNewPassword());
    }
}
