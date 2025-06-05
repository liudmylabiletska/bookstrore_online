package kristar.projects.mapper;

import kristar.projects.config.MapperConfig;
import kristar.projects.dto.BookDto;
import kristar.projects.dto.CreateBookRequestDto;
import kristar.projects.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);
}
