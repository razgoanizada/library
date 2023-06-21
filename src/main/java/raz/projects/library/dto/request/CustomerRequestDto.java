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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDto {

    @NotNull
    @Size(min = 2,  max = 30, message = "first name is must be between 2 - 30 characters")
    private String firstName;

    @NotNull
    @Size(min = 2,  max = 30, message = "last name is must be between 2 - 30 characters")
    private String lastName;

    @UniqueEmail()
    @NotNull
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,6}$")
    private String email;

    @NotNull
    @Size(min = 9, max = 11, message = "Invalid phone number") // 9: 031234567 10:0524046007 11: 052-4046007
    private String phone;

    @Pattern(regexp = "\\d{9}", message = "ID card must have 9 digits")
    @UniqueTz
    @UniqueTzIsrael
    private String tz;

    @NotNull
    private String customerTypeName;

}
