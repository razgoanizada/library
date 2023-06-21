package raz.projects.library.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import raz.projects.library.validation.CustomerTypeName.UniqueCustomerTypeName;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerTypeRequestDto {

    @NotNull
    @UniqueCustomerTypeName
    private String name;

    @NotNull
    private int days;
}
