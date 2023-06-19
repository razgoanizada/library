package raz.projects.library.dto.update;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import raz.projects.library.enums.Permissions;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LibrarianUpdate {

    @NotNull
    @Size(min = 3, max = 15, message = "full name should be between 3-15 characters")
    private String fullName;

    @NotNull
    @Size(min = 9, max = 11, message = "Invalid phone number") // 9: 031234567 10 :0524046007 11: 052-4046007
    private String phone;

    @NotNull
    private Permissions permission;
}
