package mate.academy.bookstoreonline.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.dto.auth.UserLoginRequestDto;
import mate.academy.bookstoreonline.dto.auth.UserLoginResponseDto;
import mate.academy.bookstoreonline.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreonline.dto.user.UserResponseDto;
import mate.academy.bookstoreonline.exception.RegistrationException;
import mate.academy.bookstoreonline.security.AuthenticationService;
import mate.academy.bookstoreonline.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User manager", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(
            summary = "Login for registered users",
            description = "Authenticates a user by verifying email and password. "
                    + "Returns a JWT token if credentials are valid."
    )
    UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user", description = "Creates a new user account if email is not already taken")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
             throws RegistrationException {
        return userService.register(requestDto);
    }
}
