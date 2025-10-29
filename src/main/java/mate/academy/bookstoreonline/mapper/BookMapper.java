package mate.academy.bookstoreonline.mapper;

import mate.academy.bookstoreonline.dto.BookDto;
import mate.academy.bookstoreonline.dto.CreateBookRequestDto;
import mate.academy.bookstoreonline.dto.UpdateBookRequestDto;
import mate.academy.bookstoreonline.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = mate.academy.bookstoreonline.config.MapperConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {
    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Book toModel(CreateBookRequestDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBookFromDto(UpdateBookRequestDto updateBookRequestDto, @MappingTarget Book book);
}
