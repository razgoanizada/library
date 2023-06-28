package raz.projects.library.service.Customer;

import raz.projects.library.dto.request.CustomerTypeRequestDto;
import raz.projects.library.dto.response.CustomerTypeResponseDto;

public interface CustomerTypeService {

    CustomerTypeResponseDto addCustomerType (CustomerTypeRequestDto dto);
}
