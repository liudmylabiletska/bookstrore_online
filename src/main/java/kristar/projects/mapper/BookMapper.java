package kristar.projects.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import kristar.projects.config.MapperConfig;
import kristar.projects.dto.book.BookDto;
import kristar.projects.dto.book.BookDtoWithoutCategoryIds;
import kristar.projects.dto.book.CreateBookRequestDto;
import kristar.projects.dto.book.UpdateBookRequestDto;
import kristar.projects.model.Book;
import kristar.projects.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Mapping(target = "categories", ignore = true)
    Book toModel(CreateBookRequestDto requestDto);

    @Mapping(target = "categories", ignore = true)
    void updateBookFromDto(@MappingTarget Book book, UpdateBookRequestDto requestDto);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> categoryIds = book.getCategories().stream()
                .map((category) -> category.getId())
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(categoryIds);
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto bookDto) {
        if (bookDto.getCategoryIds() != null && !bookDto.getCategoryIds().isEmpty()) {
            Set<Category> categories = bookDto.getCategoryIds().stream()
                    .map(id -> new Category(id))
                    .collect(Collectors.toSet());
            book.setCategories(categories);
        }
    }

    @AfterMapping
    default void updateCategories(@MappingTarget Book book, UpdateBookRequestDto requestDto) {
        if (requestDto.getCategoryIds() != null) {
            if (requestDto.getCategoryIds().isEmpty()) {
                book.setCategories(new HashSet<>());
            } else {
                Set<Category> categories = requestDto.getCategoryIds().stream()
                        .map(id -> {
                            Category category = new Category();
                            category.setId(id);
                            return category;
                        })
                        .collect(Collectors.toSet());
                book.setCategories(categories);
            }
        }
    }
}
