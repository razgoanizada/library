package raz.projects.library.validation.tz;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import raz.projects.library.repository.CustomerRepository;
import raz.projects.library.repository.LibrarianRepository;


public class UniqueTzValidator implements ConstraintValidator<UniqueTz, String> {

    @Autowired
    LibrarianRepository librarianRepository;

    @Autowired
    CustomerRepository customerRepository;

    private String fieldName;

    @Override
    public void initialize(UniqueTz constraintAnnotation) {

        ConstraintValidator.super.initialize(constraintAnnotation);
        fieldName = constraintAnnotation.fieldName();
    }


    @Override
    public boolean isValid(String tz, ConstraintValidatorContext context) {

        return switch (fieldName) {

            case "librarian" -> librarianRepository.findLibrarianByTzIgnoreCase(tz).isEmpty();
            case "customer" -> customerRepository.findCustomerByTzIgnoreCase(tz).isEmpty();
            default -> throw new  RuntimeException("");
        };
    }

}
