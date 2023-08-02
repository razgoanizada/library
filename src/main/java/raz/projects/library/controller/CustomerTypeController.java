package raz.projects.library.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import raz.projects.library.dto.pages.CustomerTypePageDto;
import raz.projects.library.dto.request.CustomerTypeRequestDto;
import raz.projects.library.dto.response.CustomerTypeResponseDto;
import raz.projects.library.dto.update.CustomerTypeUpdateDto;
import raz.projects.library.service.Customer.CustomerTypeService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('admin')")
@RequestMapping("/api/v1/customer-type")
public class CustomerTypeController {

    private final CustomerTypeService customerTypeService;

    @GetMapping()
    public ResponseEntity<List<CustomerTypeResponseDto>> getTypes () {

        return ResponseEntity.ok(customerTypeService.getTypes());
    }

    @GetMapping ("/page")
    public ResponseEntity<CustomerTypePageDto> getTypesPage (
            @RequestParam(value = "pageNo", required = false, defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(value = "name", required = false ) String name,
            @RequestParam(value = "days", required = false) Integer days,
            @RequestParam(value = "amount", required = false) Integer amount
    ) {

        if (days == null) {
            days = Integer.MIN_VALUE;
        }

        if (amount == null) {
            amount = Integer.MIN_VALUE;
        }

        return ResponseEntity.ok(customerTypeService.getTypesPage(
                pageNo, pageSize, sortBy, sortDir, name, days, amount));
    }

    @PostMapping("/add")
    public ResponseEntity<CustomerTypeResponseDto> addCustomerType (
            @RequestBody @Valid CustomerTypeRequestDto dto, UriComponentsBuilder uriComponentsBuilder) {

        var responseDto = customerTypeService.addCustomerType(dto);

        var uri = uriComponentsBuilder.path("/customer-type/{id}").buildAndExpand(responseDto.getId()).toUri();

        return ResponseEntity.created(uri).body(responseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerTypeResponseDto> getTypeById (@PathVariable @Valid @NotNull Long id) {

        return ResponseEntity.ok(customerTypeService.getTypeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerTypeResponseDto> updateTypeById (@PathVariable @Valid @NotNull Long id,
                                                                         @Valid @RequestBody CustomerTypeUpdateDto dto) {

        return ResponseEntity.accepted().body(customerTypeService.updateType(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerTypeResponseDto> deleteTypeById (@PathVariable @Valid @NotNull Long id) {

        return ResponseEntity.accepted().body(customerTypeService.deleteTypeById(id));

    }
}