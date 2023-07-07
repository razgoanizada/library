package raz.projects.library.dto.update;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BorrowExtraTime {

    @NotNull
    @Min(value = 1, message = "days must be greater than or equal to 1")
    @Max(value = 180, message = "days must be less than or equal to 180")
    private int days;
}
