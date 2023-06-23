package raz.projects.library.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequestDto {

    @NotNull
    @NotEmpty
    @Size(min = 3, max = 15, message = "User name should be between 3-15 characters")
    private String userName;

    @NotNull(message = "password is mandatory")
    @NotEmpty
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?\\W).{8,20}$",
            message = "The password must be at least 8 characters long, an uppercase letter, a lowercase letter, a number, and a character")
    private String password;
}
