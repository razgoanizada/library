package raz.projects.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import raz.projects.library.enums.Permissions;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibrarianResponseDto {
    private Long id;
    private String fullName;
    private String userName;
    private String email;
    private String phone;
    private String tz;
    private Set<Permissions> permission;
}
