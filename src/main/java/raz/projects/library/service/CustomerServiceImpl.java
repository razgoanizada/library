package raz.projects.library.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import raz.projects.library.dto.pages.CustomerPageDto;
import raz.projects.library.dto.pages.LibrarianPageDto;
import raz.projects.library.dto.request.CustomerRequestDto;
import raz.projects.library.dto.response.CustomerResponseDto;
import raz.projects.library.dto.response.LibrarianResponseDto;
import raz.projects.library.entity.Customer;
import raz.projects.library.entity.Librarian;
import raz.projects.library.errors.ResourceNotFoundException;
import raz.projects.library.repository.CustomerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;
    @Override
    public List<CustomerResponseDto> getCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(librarian -> mapper.map(librarian, CustomerResponseDto.class))
                .toList();
    }

    @Override
    public CustomerPageDto getCustomersPage(int pageNo, int pageSize, String sortBy, String sortDir) {


            Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);

            Page<Customer> page = customerRepository.findAll(pageable);

            return CustomerPageDto.builder()
                    .results(page.stream().map(customer -> mapper.map(customer, CustomerResponseDto.class)).toList())
                    .totalPages(page.getTotalPages())
                    .totalBooks(page.getTotalElements())
                    .isFirst(page.isFirst())
                    .isLast(page.isLast())
                    .pageNo(page.getNumber())
                    .pageSize(page.getSize())
                    .build();

    }

    @Override
    public CustomerResponseDto addCustomer(CustomerRequestDto dto) {

        var customer = mapper.map(dto, Customer.class);

        var saveCustomer = customerRepository.save(customer);
        return mapper.map(saveCustomer, CustomerResponseDto.class);

    }

    @Override
    public CustomerResponseDto getCustomerById(long id) {

        var customer = customerRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("get customer" ,id, "This customer doesn't exist in the library")
        );
        return mapper.map(customer, CustomerResponseDto.class);
    }
}
