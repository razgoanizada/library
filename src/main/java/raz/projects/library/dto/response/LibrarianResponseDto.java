package raz.projects.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raz.projects.library.enums.Gender;

import java.sql.Date;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibrarianResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String phone;
    private String tz;
    private Gender gender;
    private String address;
    private LocalDate dateOfBirth;
    private Date creationDate;
    private String permission;
}
