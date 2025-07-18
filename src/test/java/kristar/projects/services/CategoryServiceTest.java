package kristar.projects.services;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kristar.projects.dto.category.CategoryRequestDto;
import kristar.projects.dto.category.CategoryResponseDto;
import kristar.projects.mapper.CategoryMapper;
import kristar.projects.model.Category;
import kristar.projects.repository.category.CategoryRepository;
import kristar.projects.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @DisplayName("Verify findAll() method")
    @Test
    void findAll_ValidPageable_ReturnsAllCategories() {
        Category category = new Category();
        Category category1 = new Category();

        Pageable pageable = PageRequest.of(0, 20);
        CategoryResponseDto categoryDto = new CategoryResponseDto();
        CategoryResponseDto categoryDto1 = new CategoryResponseDto();
        List<Category> categoriesList = List.of(category, category1);
        Page<Category> categoriesPage = new PageImpl<>(categoriesList);

        when(categoryRepository.findAll(pageable)).thenReturn(categoriesPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);

        Page<CategoryResponseDto> actual = categoryService.findAll(pageable);

        assertEquals(2, actual.getTotalElements());

        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(2)).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @DisplayName("Verify getById() method")
    @Test
    void getById_ValidId_ReturnsCategoryDtoByID() {
        Long id = 1L;
        Category category = new Category();
        CategoryResponseDto expectedBookDto = new CategoryResponseDto();
        expectedBookDto.setId(id);

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedBookDto);

        CategoryResponseDto actualBookDto = categoryService.getById(id);

        assertEquals(expectedBookDto, actualBookDto);

        verify(categoryRepository, times(1)).findById(id);
        verifyNoMoreInteractions(categoryRepository);
    }

    @DisplayName("Verify save() method")
    @Test
    void save_ValidRequestDto_ReturnsCategoryDto() {
        CategoryRequestDto requestDto = new CategoryRequestDto();
        requestDto.setName("Test Category name");

        Category category = new Category();
        CategoryResponseDto expectedDto = new CategoryResponseDto();
        expectedDto.setName(requestDto.getName());

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when((categoryMapper.toDto(category))).thenReturn(expectedDto);

        CategoryResponseDto actualDto = categoryService.save(requestDto);

        assertEquals(expectedDto, actualDto);
        assertEquals("Test Category name", actualDto.getName());

        verify(categoryMapper, times(1)).toEntity(requestDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);

        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @DisplayName("Verify update() method")
    @Test
    void updateById_validId_shouldUpdateCategory() {
        Long id = 1L;

        CategoryRequestDto updateRequestDto = new CategoryRequestDto();
        updateRequestDto.setName("Updated Category Name");

        Category existingCategory = new Category();
        existingCategory.setId(id);
        existingCategory.setName("Old Name");

        Category updatedCategory = new Category();
        updatedCategory.setId(id);
        updatedCategory.setName(updateRequestDto.getName());

        CategoryResponseDto updatedDto = new CategoryResponseDto();
        updatedDto.setId(id);
        updatedDto.setName(updatedCategory.getName());

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        doAnswer(invocation -> {
            existingCategory.setName(updateRequestDto.getName());
            return null;
        }).when(categoryMapper).updateCategoryFromDto(existingCategory, updateRequestDto);
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(updatedDto);

        CategoryResponseDto actualDto = categoryService.update(id, updateRequestDto);

        assertEquals("Updated Category Name", actualDto.getName());
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryMapper, times(1)).updateCategoryFromDto(existingCategory, updateRequestDto);
        verify(categoryRepository, times(1)).save(existingCategory);
        verify(categoryMapper, times(1)).toDto(updatedCategory);
    }

    @DisplayName("Verify delete() method")
    @Test
    void deleteById_ValidId_ShouldInvokeRepositoryDelete() {
        Long id = 5L;

        Category category = new Category();
        category.setId(id);
        category.setName("Dummy Category");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById(id);

        assertDoesNotThrow(() -> categoryService.deleteById(id));

        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).deleteById(id);
    }
}
