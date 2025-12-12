package mate.academy.bookstoreonline.service.category;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreonline.dto.book.CategoryDto;
import mate.academy.bookstoreonline.dto.book.CategoryRequestDto;
import mate.academy.bookstoreonline.exception.EntityNotFoundException;
import mate.academy.bookstoreonline.mapper.BookMapper;
import mate.academy.bookstoreonline.mapper.CategoryMapper;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.model.Category;
import mate.academy.bookstoreonline.repository.BookRepository;
import mate.academy.bookstoreonline.repository.CategoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto save(CategoryRequestDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    public CategoryDto update(Long id, CategoryRequestDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        categoryMapper.updateFromDto(categoryDto, category);

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long categoryId, Pageable pageable) {
        List<Book> books = bookRepository.findAllByCategoriesId(categoryId, pageable);
        return books.stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }
}
