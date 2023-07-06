package raz.projects.library.service.Customer;

import raz.projects.library.dto.pages.CustomerTypePageDto;
import raz.projects.library.dto.request.CustomerTypeRequestDto;
import raz.projects.library.dto.response.CustomerTypeResponseDto;
import raz.projects.library.dto.update.CustomerTypeUpdateDto;

import java.util.List;

public interface CustomerTypeService {

    List<CustomerTypeResponseDto> getTypes ();
    CustomerTypePageDto getTypesPage (
            int pageNo, int pageSize, String sortBy, String sortDir, String name, Integer days, Integer amount);
    CustomerTypeResponseDto addCustomerType (CustomerTypeRequestDto dto);
    CustomerTypeResponseDto getTypeById(long id);
    CustomerTypeResponseDto updateType(CustomerTypeUpdateDto dto, Long id);
    CustomerTypeResponseDto deleteTypeById(Long id);
}