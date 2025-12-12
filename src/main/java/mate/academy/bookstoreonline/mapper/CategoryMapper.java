package mate.academy.bookstoreonline.mapper;

import mate.academy.bookstoreonline.config.MapperConfig;
import mate.academy.bookstoreonline.dto.book.CategoryDto;
import mate.academy.bookstoreonline.dto.book.CategoryRequestDto;
import mate.academy.bookstoreonline.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryDto);

    void updateFromDto(CategoryRequestDto dto, @MappingTarget Category category);
}
