package kristar.projects.services;

import java.nio.file.AccessDeniedException;
import kristar.projects.dto.userdto.UserRegistrationRequestDto;
import kristar.projects.dto.userdto.UserResponseDto;
import kristar.projects.exception.RegistrationException;
import kristar.projects.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    User getCurrentUser() throws AccessDeniedException;
}
