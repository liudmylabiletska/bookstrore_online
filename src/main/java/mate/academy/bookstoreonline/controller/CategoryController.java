package mate.academy.bookstoreonline.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.bookstoreonline.dto.book.CategoryDto;
import mate.academy.bookstoreonline.dto.book.CategoryRequestDto;
import mate.academy.bookstoreonline.mapper.BookMapper;
import mate.academy.bookstoreonline.repository.BookRepository;
import mate.academy.bookstoreonline.service.category.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Category", description = "Provides endpoints for managing book categories")
@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController
public class CategoryController {
    private final CategoryService categoryService;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Operation(
            summary = "Create a new category",
            description = "Allows users with 'ADMIN' authority "
                    + "to create a new category in the system. "
                    + "The request must contain valid category data."
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryDto createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.save(categoryRequestDto);
    }

    @Operation(
            summary = "Retrieve paginated categories",
            description = "Fetches a paginated list of categories with optional "
                    + "sorting and page size customization."
                    + " By default, categories are sorted in ascending order."
                    + " The sorting criteria should be specified in the format: "
                    + "sort: field,(ASC||DESC)'"
                    + " Only users with the 'USER' authority have access to this endpoint.."
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping
    public List<CategoryDto> getAll(Pageable pageable) {
        return categoryService.findAll(pageable);
    }

    @Operation(summary = "Get category by ID",
            description = "Allows users with 'USER' authority to retrieve a category by its ID."
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Update category by ID",
            description = "Allows users with 'ADMIN' authority to update the details of a category."
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        return categoryService.update(id, categoryRequestDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete category by ID",
            description = "Allows users with 'ADMIN' authority to delete a category by its ID."
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteById(id);
    }

    @Operation(summary = "Get books by category ID",
            description = "Allows users with 'USER' authority to retrieve a list of books "
                    + "that belong to the specified category."
    )
    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping("/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(@PathVariable Long id, Pageable pageable) {
        return categoryService.getBooksByCategoryId(id,pageable);
    }
}
