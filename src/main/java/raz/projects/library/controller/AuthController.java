package raz.projects.library.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raz.projects.library.dto.request.SignInRequestDto;
import raz.projects.library.security.JWTProvider;
import raz.projects.library.service.LibrarianServiceImpl;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/online-library/login")
public class AuthController {

    private final LibrarianServiceImpl authService;
    private final JWTProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @PostMapping()
    public ResponseEntity<Object> signIn(@RequestBody @Valid SignInRequestDto dto) {
        var user = authService.loadUserByUsername(dto.getUserName());

        var savedPassword = user.getPassword();
        var givenPassword = dto.getPassword();

        if (passwordEncoder.matches(givenPassword, savedPassword)) {

            var token = jwtProvider.generateToken(user.getUsername());

            return ResponseEntity.ok(Map.of("jwt", token));
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
