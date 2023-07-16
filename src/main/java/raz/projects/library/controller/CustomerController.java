package raz.projects.library.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import raz.projects.library.dto.pages.CustomerPageDto;
import raz.projects.library.dto.request.CustomerRequestDto;
import raz.projects.library.dto.response.CustomerResponseDto;
import raz.projects.library.dto.update.CustomerUpdate;
import raz.projects.library.service.Customer.CustomerService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getCustomers () {

        return ResponseEntity.ok(customerService.getCustomers());
    }

    @GetMapping ("/page")
    public ResponseEntity<CustomerPageDto> getCustomersPage (
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "tz", required = false) String tz,
            @RequestParam(value = "addedBy", required = false) String addedBy,
            @RequestParam(value = "isActive", required = false, defaultValue = "true") Boolean isActive
    ) {

        return ResponseEntity.ok(customerService.getCustomersPage(
                pageNo, pageSize, sortBy, sortDir, type, firstName, lastName, phone, tz, addedBy, isActive));
    }

    @PostMapping("/add")
    public ResponseEntity<CustomerResponseDto> addCustomer (
            @RequestBody @Valid CustomerRequestDto dto,
            UriComponentsBuilder uriComponentsBuilder,
            Authentication authentication) {

        var responseDto = customerService.addCustomer(dto, authentication);

        var uri = uriComponentsBuilder.path("/customers/{id}").buildAndExpand(responseDto.getId()).toUri();

        return ResponseEntity.created(uri).body(responseDto);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById (@PathVariable @Valid @NotNull Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer (@PathVariable @Valid @NotNull Long id,
                                                               @Valid @RequestBody CustomerUpdate dto) {

        return ResponseEntity.accepted().body(customerService.updateCustomerById(dto, id));
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<CustomerResponseDto> updateIsActive (@PathVariable @Valid @NotNull Long id) {

        return ResponseEntity.accepted().body(customerService.isActive(id));
    }


}