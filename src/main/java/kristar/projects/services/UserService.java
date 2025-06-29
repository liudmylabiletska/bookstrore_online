package kristar.projects.services;

import kristar.projects.dto.userdto.UserRegistrationRequestDto;
import kristar.projects.dto.userdto.UserResponseDto;
import kristar.projects.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
