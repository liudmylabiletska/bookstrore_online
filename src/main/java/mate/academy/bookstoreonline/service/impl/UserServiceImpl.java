package mate.academy.bookstoreonline.service.impl;

import mate.academy.bookstoreonline.dto. user.UserResponseDto;
import mate.academy.bookstoreonline.model.User;
import mate.academy.bookstoreonline.exception.RegistrationException;
import mate.academy.bookstoreonline.mapper.UserMapper;
import mate.academy.bookstoreonline.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreonline.service.UserService;
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
            throw new RegistrationException(
                    "Email already exists: " + requestDto.getEmail()
            );
        }
        User user = userMapper.toUser(requestDto);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
