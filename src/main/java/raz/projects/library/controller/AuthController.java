package raz.projects.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import raz.projects.library.dto.request.SignInRequestDto;
import raz.projects.library.dto.response.SignInResponseDto;
import raz.projects.library.dto.update.LibrarianChangePassword;
import raz.projects.library.security.JWTProvider;
import raz.projects.library.service.Librarian.LibrarianService;
import raz.projects.library.service.Librarian.LibrarianServiceImpl;
import raz.projects.library.service.Log.LogService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {

    private final LibrarianService librarianService;
    private final LibrarianServiceImpl authService;
    private final LogService logService;
    private final JWTProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<SignInResponseDto> signIn(
            @RequestBody @Valid SignInRequestDto dto, HttpServletRequest httpServletRequest) {

        try {

            var user = authService.loadUserByUsername(dto.getUserName());
            var savedPassword = user.getPassword();
            var givenPassword = dto.getPassword();


            if (passwordEncoder.matches(givenPassword, savedPassword)) {

                var token = jwtProvider.generateToken(user.getUsername());

                var permission = authService.getLibrarianByUserName(dto.getUserName()).getPermission().getPermission();

                authService.updateLibrarianLastLogin(user.getUsername());

                logService.logLoginAttempt(dto.getUserName(), httpServletRequest.getRemoteAddr(), true);

                return ResponseEntity.ok(new SignInResponseDto(token, permission));
            }
        } catch (Exception e) {
            logService.logLoginAttempt(dto.getUserName(), httpServletRequest.getRemoteAddr(), false);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        logService.logLoginAttempt(dto.getUserName(), httpServletRequest.getRemoteAddr(), false);
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }


    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword (@Valid @RequestBody LibrarianChangePassword dto,
                                                  Authentication authentication) {
        var librarian = librarianService.getLibrarianByUserName(authentication.getName());
        return ResponseEntity.accepted().body(authService.change(dto, librarian));

    }


}
