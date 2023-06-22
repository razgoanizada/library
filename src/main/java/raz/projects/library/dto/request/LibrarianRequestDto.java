package raz.projects.library.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import raz.projects.library.validation.email.UniqueEmail;
import raz.projects.library.validation.tz.UniqueTz;
import raz.projects.library.validation.tz.UniqueTzIsrael;
import raz.projects.library.validation.userName.UniqueUserName;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibrarianRequestDto {

    @NotNull
    @Size(min = 3, max = 15, message = "full name should be between 3-15 characters")
    private String fullName;

    @NotNull
    @UniqueUserName
    @Size(min = 3, max = 15, message = "User name should be between 3-15 characters")
    private String userName;

    @NotNull
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?\\W).{8,20}$",
            message = "The password must be at least 8 characters long, an uppercase letter, a lowercase letter, a number, and a character")
    private String password;

    @NotNull
    @UniqueEmail(fieldName = "librarian")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,6}$")
    private String email;

    @NotNull
    @Size(min = 9, max = 11, message = "Invalid phone number") // 9: 031234567 10 :0524046007 11: 052-4046007
    private String phone;

    @Pattern(regexp = "\\d{9}", message = "ID card must have 9 digits")
    @UniqueTz (fieldName = "librarian")
    @UniqueTzIsrael
    private String tz;

    @NotNull
    private String permission;
}
