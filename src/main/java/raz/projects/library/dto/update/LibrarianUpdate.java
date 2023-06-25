package raz.projects.library.dto.update;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibrarianUpdate {

    @NotNull
    @Size(min = 2,  max = 30, message = "first name is must be between 2 - 30 characters")
    private String firstName;

    @NotNull
    @Size(min = 2,  max = 30, message = "last name is must be between 2 - 30 characters")
    private String lastName;

    @NotNull
    @Size(min = 9, max = 11, message = "Invalid phone number") // 9: 031234567 10 :0524046007 11: 052-4046007
    private String phone;

    @NotNull
    private String permission;
}
