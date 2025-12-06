package mate.academy.bookstoreonline.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreonline.dto.user.UserResponseDto;
import mate.academy.bookstoreonline.exception.EntityNotFoundException;
import mate.academy.bookstoreonline.exception.RegistrationException;
import mate.academy.bookstoreonline.mapper.UserMapper;
import mate.academy.bookstoreonline.model.User;
import mate.academy.bookstoreonline.model.Role;
import mate.academy.bookstoreonline.model.enums.RoleName;
import mate.academy.bookstoreonline.repository.RoleRepository;
import mate.academy.bookstoreonline.repository.UserRepository;
import mate.academy.bookstoreonline.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new RegistrationException("Can not create a new User, "
                    + "this email already exist");
        }
        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role userRole = roleRepository.findByRoleName(RoleName.USER)
                .orElseThrow(() -> new EntityNotFoundException("Can't find role by name: "
                        + RoleName.USER.name()));
        user.setRoles(Set.of(userRole));
        return userMapper.toDto(userRepository.save(user));
    }
}
