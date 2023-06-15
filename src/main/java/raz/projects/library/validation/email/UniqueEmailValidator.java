package raz.projects.library.validation.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import raz.projects.library.repository.CustomerRepository;
import raz.projects.library.repository.LibrarianRepository;


public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
   LibrarianRepository librarianRepository;

    @Autowired
    CustomerRepository customerRepository;

    private String fieldName;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {

     ConstraintValidator.super.initialize(constraintAnnotation);
     fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid (String email, ConstraintValidatorContext context) {

        return switch (fieldName) {

            case "librarian" -> librarianRepository.findLibrarianByEmailIgnoreCase(email).isEmpty();
            case "customer" -> customerRepository.findCustomerByEmailIgnoreCase(email).isEmpty();
            default -> throw new  RuntimeException("");
        };
    }
}
