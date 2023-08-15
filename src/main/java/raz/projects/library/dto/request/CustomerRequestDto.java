package raz.projects.library.dto.request;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import raz.projects.library.enums.Gender;
import raz.projects.library.validation.email.UniqueEmail;
import raz.projects.library.validation.tz.UniqueTz;
import raz.projects.library.validation.tz.UniqueTzIsrael;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDto {

    @NotNull
    @Size(min = 2,  max = 10, message = "first name is must be between 2 - 30 characters")
    private String firstName;

    @NotNull
    @Size(min = 2,  max = 10, message = "last name is must be between 2 - 30 characters")
    private String lastName;

    @NotNull
    @UniqueEmail()
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
    private Gender gender;

    private String address;

    @Past(message = "Date of birth should be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate DateOfBirth;

    @NotNull
    private String customerTypeName;

}