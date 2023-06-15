package raz.projects.library.service;


import raz.projects.library.dto.pages.CustomerPageDto;
import raz.projects.library.dto.request.CustomerRequestDto;
import raz.projects.library.dto.response.CustomerResponseDto;

import java.util.List;

public interface CustomerService {

    List<CustomerResponseDto> getCustomers ();

    CustomerPageDto getCustomersPage(int pageNo, int pageSize, String sortBy, String sortDir);
    CustomerResponseDto addCustomer (CustomerRequestDto dto);

    CustomerResponseDto getCustomerById(long id);

}
