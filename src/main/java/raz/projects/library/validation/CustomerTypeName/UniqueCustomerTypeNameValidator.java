package raz.projects.library.validation.CustomerTypeName;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import raz.projects.library.entity.CustomerType;
import raz.projects.library.repository.CustomerTypeRepository;

@RequiredArgsConstructor
public class UniqueCustomerTypeNameValidator implements ConstraintValidator<UniqueCustomerTypeName, String> {

    private final CustomerTypeRepository customerTypeRepository;
    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {

        CustomerType customerType = customerTypeRepository.findCustomerTypeByNameIgnoreCase(name);

        return customerType == null;
    }
}