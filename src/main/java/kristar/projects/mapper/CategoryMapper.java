package kristar.projects.mapper;

import kristar.projects.config.MapperConfig;
import kristar.projects.dto.categorydto.CategoryRequestDto;
import kristar.projects.dto.categorydto.CategoryResponseDto;
import kristar.projects.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = CategoryMapper.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toEntity(CategoryRequestDto categoryRequestDto);

    void updateCategoryFromDto(@MappingTarget Category category, CategoryRequestDto requestDto);
}
