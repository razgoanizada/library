package raz.projects.library.service.Customer;


import org.springframework.security.core.Authentication;
import raz.projects.library.dto.pages.CustomerPageDto;
import raz.projects.library.dto.request.CustomerRequestDto;
import raz.projects.library.dto.response.CustomerResponseDto;
import raz.projects.library.dto.update.CustomerUpdate;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseDto> getCustomers ();

    CustomerPageDto getCustomersPage(
            int pageNo, int pageSize, String sortBy, String sortDir,
            String customerType, String firstName, String lastName, String phone, String tz, String addedBy, Boolean isActive);
    CustomerResponseDto addCustomer(CustomerRequestDto dto, Authentication authentication);
    CustomerResponseDto getCustomerById(long id);
    CustomerResponseDto updateCustomerById (CustomerUpdate dto, long id);
    CustomerResponseDto isActive (long id);

}