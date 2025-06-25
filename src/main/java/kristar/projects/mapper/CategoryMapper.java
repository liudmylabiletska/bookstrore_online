package kristar.projects.mapper;

import kristar.projects.config.MapperConfig;
import kristar.projects.dto.categorydto.CategoryDto;
import kristar.projects.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CategoryDto categoryDto);
}
