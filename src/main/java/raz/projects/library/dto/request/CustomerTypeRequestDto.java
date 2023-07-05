package raz.projects.library.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @Max(180)
    private int days;

    @NotNull
    @Max(10)
    private int amountBooks;
}
