package kristar.projects.mapper;

import kristar.projects.config.MapperConfig;
import kristar.projects.dto.userdto.UserRegistrationRequestDto;
import kristar.projects.dto.userdto.UserResponseDto;
import kristar.projects.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = UserMapper.class)
public interface UserMapper {
    UserResponseDto toUserResponse(User user);

    User toModel(UserRegistrationRequestDto registrationRequestDto);
}
