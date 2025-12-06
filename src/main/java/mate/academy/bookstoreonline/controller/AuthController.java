package mate.academy.bookstoreonline.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreonline.dto.user.UserResponseDto;
import mate.academy.bookstoreonline.exception.RegistrationException;
import mate.academy.bookstoreonline.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User manager", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/registration")
    @Operation(summary = "Register a new user", description = "Creates a new user account if email is not already taken")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
             throws RegistrationException {
        return userService.register(requestDto);
    }
}
