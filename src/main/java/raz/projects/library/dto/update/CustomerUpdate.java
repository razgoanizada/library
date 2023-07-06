package raz.projects.library.dto.update;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import raz.projects.library.enums.Gender;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdate {

    @NotNull
    @Size(min = 2,  max = 30, message = "first name is must be between 2 - 30 characters")
    private String firstName;

    @NotNull
    @Size(min = 2,  max = 30, message = "last name is must be between 2 - 30 characters")
    private String lastName;

    @NotNull
    @Size(min = 9, max = 11, message = "Invalid phone number") // 9: 031234567 10:0524046007 11: 052-4046007
    private String phone;

    private Gender gender;

    private String address;

    @Past(message = "Date of birth should be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate DateOfBirth;

    @NotNull
    private String customerTypeName;
}