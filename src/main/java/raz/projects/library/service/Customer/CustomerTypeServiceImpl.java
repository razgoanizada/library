package raz.projects.library.service.Customer;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import raz.projects.library.dto.request.CustomerTypeRequestDto;
import raz.projects.library.dto.response.CustomerTypeResponseDto;
import raz.projects.library.entity.CustomerType;
import raz.projects.library.repository.CustomerTypeRepository;

@Service
@RequiredArgsConstructor
public class CustomerTypeServiceImpl implements CustomerTypeService {

    private final CustomerTypeRepository customerTypeRepository;
    private final ModelMapper mapper;
    @Override
    public CustomerTypeResponseDto addCustomerType(CustomerTypeRequestDto dto) {

      var customerType = mapper.map(dto, CustomerType.class);
      var saveCustomerType = customerTypeRepository.save(customerType);
      return mapper.map(saveCustomerType, CustomerTypeResponseDto.class);
    }
}
