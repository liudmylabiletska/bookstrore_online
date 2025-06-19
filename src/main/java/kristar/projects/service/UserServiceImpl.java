package kristar.projects.service;

import kristar.projects.dto.userdto.UserRegistrationRequestDto;
import kristar.projects.dto.userdto.UserResponseDto;
import kristar.projects.exception.RegistrationException;
import kristar.projects.mapper.UserMapper;
import kristar.projects.model.User;
import kristar.projects.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can not register new user,"
                    + " because inserted email " + requestDto.getEmail()
                    + " is already present in the DB");
        }
        User userFromRequestDto = userMapper.toModel(requestDto);
        User savedUser = userRepository.save(userFromRequestDto);
        return userMapper.toUserResponse(savedUser);
    }
}
