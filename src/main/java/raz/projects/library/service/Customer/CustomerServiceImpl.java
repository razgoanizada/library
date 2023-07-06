package raz.projects.library.service.Customer;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import raz.projects.library.dto.pages.CustomerPageDto;
import raz.projects.library.dto.request.CustomerRequestDto;
import raz.projects.library.dto.response.CustomerResponseDto;
import raz.projects.library.dto.update.CustomerUpdate;
import raz.projects.library.entity.Customer;
import raz.projects.library.entity.CustomerType;
import raz.projects.library.errors.BadRequestException;
import raz.projects.library.errors.ResourceNotFoundException;
import raz.projects.library.repository.CustomerRepository;
import raz.projects.library.repository.CustomerTypeRepository;
import raz.projects.library.repository.LibrarianRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerTypeRepository customerTypeRepository;
    private final LibrarianRepository librarianRepository;
    private final ModelMapper mapper;
    @Override
    public List<CustomerResponseDto> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> mapper.map(customer, CustomerResponseDto.class))
                .toList();
    }

    @Override
    public CustomerPageDto getCustomersPage(
            int pageNo, int pageSize, String sortBy, String sortDir,
            String type, String firstName, String lastName, String phone, String tz, String addedBy, Boolean isActive) {

        Specification<Customer> specification = Specification.where(null);

        if (type != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("type"), type));
        }

        if (firstName != null && !firstName.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("firstName")), "%" + firstName.toLowerCase() + "%" ));
        }

        if (lastName != null && !lastName.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("lastName")), "%" + lastName.toLowerCase() + "%" ));
        }

        if (phone != null && !phone.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("phone")), "%" + phone.toLowerCase() + "%" ));
        }

        if (tz != null && !tz.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("tz")), "%" + tz.toLowerCase() + "%" ));
        }

        if (addedBy != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("addedBy"), addedBy));
        }

        if (isActive != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("isActive"), isActive));
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);

        Page<Customer> page = customerRepository.findAll(specification ,pageable);

        return CustomerPageDto.builder()
                .results(page.stream().map(customer -> mapper.map(customer, CustomerResponseDto.class)).toList())
                .totalPages(page.getTotalPages())
                .totalCustomers(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .build();

    }

    @Override
    public CustomerResponseDto addCustomer(CustomerRequestDto dto, Authentication authentication) {

        CustomerType customerType = customerTypeRepository
                .findCustomerTypeByNameIgnoreCase(dto.getCustomerTypeName());

        if (customerType == null) {
            throw new BadRequestException(
                    "add customer",
                    dto.getCustomerTypeName(),
                    "This customer type doesn't exist in the library");
        }

        var librarian = librarianRepository.findLibrarianByUserNameIgnoreCase(authentication.getName()).orElseThrow();

        var customer = mapper.map(dto, Customer.class);

        customer.setCustomerType(customerType);
        customer.setAddedBy(librarian);
        customer.setActive(true);

        customerRepository.save(customer);
        var response =  mapper.map(customer, CustomerResponseDto.class);
        response.setCustomerTypeName(customer.getCustomerType().getName());
        response.setAddedByUserName(customer.getAddedBy().getUserName());

        return response;

    }

    @Override
    public CustomerResponseDto getCustomerById(long id) {

        var customer = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("get customer" ,id, "This customer doesn't exist in the library")
        );
        return mapper.map(customer, CustomerResponseDto.class);
    }

    @Override
    public CustomerResponseDto updateCustomerById(CustomerUpdate dto, long id) {

        var customer = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("update customer" ,id, "This customer doesn't exist in the library")
        );

        CustomerType customerType =  customerTypeRepository
                .findCustomerTypeByNameIgnoreCase(dto.getCustomerTypeName());

        if (customerType == null) {
            throw new BadRequestException(
                    "update book - categories",
                    dto.getCustomerTypeName(),
                    "This categories doesn't exist in the library");
        }

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setPhone(dto.getPhone());
        customer.setGender(dto.getGender());
        customer.setAddress(dto.getAddress());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setCustomerType(customerType);

        customerRepository.save(customer);
        var response = mapper.map(customer, CustomerResponseDto.class);

        response.setCustomerTypeName(customer.getCustomerType().getName());
        response.setAddedByUserName(customer.getAddedBy().getUserName());

        return response;
    }

    @Override
    public CustomerResponseDto isActive(long id) {

        var customer = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("update customer - is active" ,id, "This customer doesn't exist in the library")
        );

        customer.setActive(!customer.isActive());

        var save = customerRepository.save(customer);

        return mapper.map(save, CustomerResponseDto.class);
    }
}