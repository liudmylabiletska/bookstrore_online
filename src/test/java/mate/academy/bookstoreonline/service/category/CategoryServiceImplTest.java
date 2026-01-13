package mate.academy.bookstoreonline.service.category;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.math.BigDecimal;
import mate.academy.bookstoreonline.dto.book.CategoryDto;
import mate.academy.bookstoreonline.dto.book.CategoryRequestDto;
import mate.academy.bookstoreonline.exception.EntityNotFoundException;
import mate.academy.bookstoreonline.mapper.CategoryMapper;
import mate.academy.bookstoreonline.model.Category;
import mate.academy.bookstoreonline.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category createCategory(Long id, String name, String desc) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(desc);
        return category;
    }

    @Test
    @DisplayName("Given valid CategoryRequestDto, when save called, then category is saved")
    void save_ValidRequest_Success() {
        CategoryRequestDto request = new CategoryRequestDto();
        request.setTitle("Programming");
        request.setAuthor("Author");
        request.setIsbn("12345");
        request.setPrice(BigDecimal.TEN);
        request.setDescription("Tech books");

        Category mapped = createCategory(null, "Programming", "Tech books");
        Category saved = createCategory(1L, "Programming", "Tech books");
        CategoryDto expected = new CategoryDto();
        expected.setId(1L);
        expected.setName("Programming");

        when(categoryMapper.toEntity(request)).thenReturn(mapped);
        when(categoryRepository.save(mapped)).thenReturn(saved);
        when(categoryMapper.toDto(saved)).thenReturn(expected);

        CategoryDto actual = categoryService.save(request);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
        verify(categoryMapper).toEntity(request);
        verify(categoryRepository).save(mapped);
    }

    @Test
    @DisplayName("Given existing category, when update called, then fields are updated")
    void update_ExistingCategory_Success() {
        Long categoryId = 1L;
        Category existing = createCategory(categoryId, "Old Name", "Old Desc");

        CategoryRequestDto request = new CategoryRequestDto();
        request.setTitle("Updated Name");
        request.setAuthor("Author");
        request.setIsbn("12345");
        request.setPrice(BigDecimal.TEN);
        request.setDescription("Updated Description");

        Category saved = createCategory(categoryId, "Updated Name", "Updated Description");
        CategoryDto expectedDto = new CategoryDto();
        expectedDto.setId(categoryId);
        expectedDto.setName("Updated Name");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> {
            CategoryRequestDto dto = invocation.getArgument(0);
            Category category = invocation.getArgument(1);
            category.setName(dto.getTitle()); // Мапер мапить title -> name
            category.setDescription(dto.getDescription());
            return null;
        }).when(categoryMapper).updateFromDto(request, existing);

        when(categoryRepository.save(existing)).thenReturn(saved);
        when(categoryMapper.toDto(saved)).thenReturn(expectedDto);

        CategoryDto actual = categoryService.update(categoryId, request);

        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedDto);
        verify(categoryMapper).updateFromDto(request, existing);
    }

    @Test
    @DisplayName("Given non-existing category, when update called, then throw exception")
    void update_NonExistingCategory_ThrowsException() {
        Long categoryId = 99L;
        CategoryRequestDto request = new CategoryRequestDto();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.update(categoryId, request));

        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Given valid ID, when getById called, then return CategoryDto")
    void getById_ValidId_ReturnsDto() {
        Long id = 1L;
        Category category = createCategory(id, "Fiction", "Stories");
        CategoryDto expected = new CategoryDto();
        expected.setId(id);
        expected.setName("Fiction");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryDto actual = categoryService.getById(id);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("Delete category by valid ID")
    void deleteById_ValidId_Success() {
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(categoryId);
        categoryService.deleteById(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }
}
