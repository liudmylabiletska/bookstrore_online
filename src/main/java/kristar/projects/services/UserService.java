package kristar.projects.services;

import kristar.projects.dto.user.UserRegistrationRequestDto;
import kristar.projects.dto.user.UserResponseDto;
import kristar.projects.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
