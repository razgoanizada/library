package raz.projects.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import raz.projects.library.dto.request.CustomerTypeRequestDto;
import raz.projects.library.dto.response.CustomerTypeResponseDto;
import raz.projects.library.service.CustomerTypeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/online-library/settings/customer-type")
public class CustomerTypeController {

    private final CustomerTypeService customerTypeService;

    @PreAuthorize("hasAuthority(T(raz.projects.library.enums.Permissions).admin)")
    @PostMapping("/add")
    public ResponseEntity<CustomerTypeResponseDto> addCustomerType (
            @RequestBody @Valid CustomerTypeRequestDto dto, UriComponentsBuilder uriComponentsBuilder) {

        var responseDto = customerTypeService.addCustomerType(dto);

        var uri = uriComponentsBuilder.path("/customer-type/{id}").buildAndExpand(responseDto.getId()).toUri();

        return ResponseEntity.created(uri).body(responseDto);
    }
}
