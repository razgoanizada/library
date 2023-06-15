package raz.projects.library.validation.userName;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import raz.projects.library.entity.Librarian;
import raz.projects.library.repository.LibrarianRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UniqueUserNameValidator implements ConstraintValidator<UniqueUserName, String> {

    private final LibrarianRepository librarianRepository;

    @Override
    public boolean isValid(String userName, ConstraintValidatorContext context) {

        Optional<Librarian> librarian = librarianRepository.findLibrarianByUserNameIgnoreCase(userName);

        return librarian.isEmpty();
    }
}
