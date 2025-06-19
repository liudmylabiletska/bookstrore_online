package kristar.projects.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kristar.projects.dto.userdto.UserRegistrationRequestDto;
import kristar.projects.dto.userdto.UserResponseDto;
import kristar.projects.exception.RegistrationException;
import kristar.projects.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Authentication and Registration APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;

    @Operation(summary = "User registration", description = "Register a new user account")
    @PostMapping("/registration")
    public UserResponseDto registerUser(@RequestBody UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }
}
