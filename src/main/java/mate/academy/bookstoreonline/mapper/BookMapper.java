package mate.academy.bookstoreonline.mapper;

import mate.academy.bookstoreonline.config.MapperConfig;
import mate.academy.bookstoreonline.dto.BookDto;
import mate.academy.bookstoreonline.dto.CreateBookRequestDto;
import mate.academy.bookstoreonline.dto.UpdateBookRequestDto;
import mate.academy.bookstoreonline.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreonline.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Book toModel(CreateBookRequestDto dto);

    Book toEntity(CreateBookRequestDto bookDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    List<BookDtoWithoutCategoryIds> toDtoWithoutCategories(List<Book> books);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateBookFromDto(UpdateBookRequestDto dto, @MappingTarget Book book);
}
