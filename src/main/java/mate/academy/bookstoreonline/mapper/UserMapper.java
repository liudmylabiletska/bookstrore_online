package mate.academy.bookstoreonline.mapper;

import mate.academy.bookstoreonline.config.MapperConfig;
import mate.academy.bookstoreonline.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreonline.dto.user.UserResponseDto;
import mate.academy.bookstoreonline.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    User toModel(UserRegistrationRequestDto dto);

    UserResponseDto toDto(User user);
}
