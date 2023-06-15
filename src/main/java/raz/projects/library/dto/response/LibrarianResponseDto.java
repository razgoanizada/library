package raz.projects.library.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
