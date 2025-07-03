package kristar.projects.mapper;

import kristar.projects.config.MapperConfig;
import kristar.projects.dto.user.UserRegistrationRequestDto;
import kristar.projects.dto.user.UserResponseDto;
import kristar.projects.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toUserResponse(User user);

    User toModel(UserRegistrationRequestDto registrationRequestDto);
}
