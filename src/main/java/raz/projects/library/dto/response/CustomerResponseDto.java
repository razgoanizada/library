package raz.projects.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raz.projects.library.enums.Gender;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String tz;
    private Gender gender;
    private String address;
    private LocalDate DateOfBirth;
    private boolean isActive;
    private String customerTypeName;
    private String addedByUserName;
    private Date CreationDate;
    private List<BorrowResponseDto> borrows;
}