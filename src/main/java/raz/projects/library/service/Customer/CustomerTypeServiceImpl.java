package raz.projects.library.service.Customer;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raz.projects.library.dto.pages.CustomerTypePageDto;
import raz.projects.library.dto.request.CustomerTypeRequestDto;
import raz.projects.library.dto.response.CustomerTypeResponseDto;
import raz.projects.library.dto.update.CustomerTypeUpdateDto;
import raz.projects.library.entity.CustomerType;
import raz.projects.library.errors.BadRequestException;
import raz.projects.library.errors.ResourceNotFoundException;
import raz.projects.library.repository.CustomerTypeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerTypeServiceImpl implements CustomerTypeService {

    private final CustomerTypeRepository customerTypeRepository;
    private final ModelMapper mapper;

    @Override
    public List<CustomerTypeResponseDto> getTypes() {

        return customerTypeRepository.findAll()
                .stream()
                .map(type -> mapper.map(type, CustomerTypeResponseDto.class))
                .toList();
    }

    @Override
    public CustomerTypePageDto getTypesPage (
            int pageNo, int pageSize, String sortBy, String sortDir, String name, Integer days, Integer amount) {

        Specification<CustomerType> specification = Specification.where(null);

        if (name != null && !name.isEmpty()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(criteriaBuilder.lower(
                            root.get("name")), "%" + name.toLowerCase() + "%" ));
        }

        if (days > 0) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("days"), days));
        }

        if (amount > 0) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("amount"), amount));
        }


        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.Direction.fromString(sortDir), sortBy);

        Page<CustomerType> page = customerTypeRepository.findAll(specification ,pageable);

        return CustomerTypePageDto.builder()
                .results(page.stream().map(type -> mapper.map(type, CustomerTypeResponseDto.class)).toList())
                .totalPages(page.getTotalPages())
                .totalTypes(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .build();
    }


    @Override
    public CustomerTypeResponseDto addCustomerType(CustomerTypeRequestDto dto) {

        var customerType = mapper.map(dto, CustomerType.class);

        try {
            customerTypeRepository.save(customerType);
            return mapper.map(customerType, CustomerTypeResponseDto.class);
        }
        catch (DataIntegrityViolationException exception) {
            throw new BadRequestException(
                    "add customer - type ",  "Days and amount must be unique");
        }

    }

    @Override
    public CustomerTypeResponseDto getTypeById(long id) {

        var customerType = customerTypeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(
                        "get customer - type" ,id, "This customer - type doesn't exist in the library")
        );
        return mapper.map(customerType, CustomerTypeResponseDto.class);
    }

    @Override
    public CustomerTypeResponseDto updateType(CustomerTypeUpdateDto dto, Long id) {

        var customerType = customerTypeRepository.findById(id).orElseThrow(
                () -> new BadRequestException(
                        "update customer - type", id, "This customer - type doesn't exist in the library")
        );

        customerType.setDays(dto.getDays());
        customerType.setAmount(dto.getAmount());


        try {
            customerTypeRepository.save(customerType);
            return mapper.map(customerType, CustomerTypeResponseDto.class);
        }
        catch (DataIntegrityViolationException exception) {
            throw new BadRequestException(
                    "update customer - type ",  "Days and amount must be unique");
        }
    }

    @Override
    public CustomerTypeResponseDto deleteTypeById(Long id) {

        var exists = customerTypeRepository.existsById(id);
        var customerType = customerTypeRepository.findById(id).orElseThrow(
                () -> new BadRequestException(
                        "delete customer - type", id, "This customer - type doesn't exist in the library")
        );

        if (exists)
            customerTypeRepository.deleteById(id);

        return mapper.map(customerType, CustomerTypeResponseDto.class);
    }

}