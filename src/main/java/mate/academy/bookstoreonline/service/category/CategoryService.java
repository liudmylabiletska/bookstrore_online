package mate.academy.bookstoreonline.service.category;


import mate.academy.bookstoreonline.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreonline.dto.book.CategoryDto;
import mate.academy.bookstoreonline.dto.book.CategoryRequestDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryRequestDto categoryDto);

    CategoryDto update(Long id, CategoryRequestDto categoryDto);

    void deleteById(Long id);

    List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long categoryId, Pageable pageable);
}
