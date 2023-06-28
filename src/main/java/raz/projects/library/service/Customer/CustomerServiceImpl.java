package raz.projects.library.service.Customer;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerTypeRepository customerTypeRepository;
    private final ModelMapper mapper;
    @Override
    public List<CustomerResponseDto> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> mapper.map(customer, CustomerResponseDto.class))
                .toList();
    }

    @Override
    public CustomerPageDto getCustomersPage(int pageNo, int pageSize, String sortBy, String sortDir) {


            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);

            Page<Customer> page = customerRepository.findAll(pageable);

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
    public CustomerResponseDto addCustomer(CustomerRequestDto dto) {

        CustomerType customerType = customerTypeRepository.findCustomerTypeByNameIgnoreCase(dto.getCustomerTypeName());

        if (customerType == null) {
            throw new BadRequestException(
                    "add customer type name",
                    dto.getCustomerTypeName(),
                    "This customer type doesn't exist in the library");
        }

        var customer = mapper.map(dto, Customer.class);
        customer.setCustomerType(customerType);
        customer.setActive(true);

       customerRepository.save(customer);
        var response =  mapper.map(customer, CustomerResponseDto.class);
        response.setCustomerTypeName(customer.getCustomerType().getName());

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

        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setPhone(dto.getPhone());
        customer.getCustomerType().setName(dto.getCustomerTypeName());

        var response = mapper.map(customer, CustomerResponseDto.class);
        response.setCustomerTypeName(customer.getCustomerType().getName());

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
