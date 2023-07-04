package raz.projects.library.dto.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import raz.projects.library.validation.password.PasswordMatch;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatch
public class LibrarianChangePassword {

    @NotNull
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?\\W).{8,20}$",
            message = "The password must be at least 8 characters long," +
                    " an uppercase letter, a lowercase letter, a number, and a character")
    private String newPassword;
    private String repeatNewPassword;
}
