package raz.projects.library.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(value = 1, message = "days must be greater than or equal to 1")
    @Max(value = 180, message = "days must be less than or equal to 180")
    private int days;

    @NotNull
    @Min(value = 1, message = "amount must be greater than or equal to 1")
    @Max(value = 10, message = "amount must be less than or equal to 10")
    private int amount;
}