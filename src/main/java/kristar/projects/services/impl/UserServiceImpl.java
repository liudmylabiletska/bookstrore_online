package kristar.projects.services.impl;

import static kristar.projects.model.RoleName.USER;

import java.nio.file.AccessDeniedException;
import java.util.HashSet;
import java.util.Set;
import kristar.projects.dto.userdto.UserRegistrationRequestDto;
import kristar.projects.dto.userdto.UserResponseDto;
import kristar.projects.exception.RegistrationException;
import kristar.projects.mapper.UserMapper;
import kristar.projects.model.Role;
import kristar.projects.model.User;
import kristar.projects.repository.role.RoleRepository;
import kristar.projects.repository.user.UserRepository;
import kristar.projects.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Can not register new user,"
                    + " because inserted email " + requestDto.getEmail()
                    + " is already present in the DB");
        }
        User userFromRequestDto = userMapper.toModel(requestDto);
        userFromRequestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(USER)
                .orElseThrow(() -> new RegistrationException("User role: " + USER + " not found"));
        roles.add(userRole);

        userFromRequestDto.setRoles(roles);

        User savedUser = userRepository.save(userFromRequestDto);
        return userMapper.toUserResponse(savedUser);
    }

    @Override
    public User getCurrentUser() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("You should be authorized");
        }
        String userEmail = authentication.getName();

        return userRepository.findByEmail(authentication.getName()).orElseThrow(()
                -> new UsernameNotFoundException("User not found by email" + userEmail));
    }
}
