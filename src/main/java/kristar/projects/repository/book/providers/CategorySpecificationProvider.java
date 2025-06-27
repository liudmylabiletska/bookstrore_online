package kristar.projects.repository.book.providers;

import kristar.projects.dto.bookdto.BookSearchParametersDto;
import kristar.projects.exception.DataProcessingException;
import kristar.projects.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("category")
public class CategorySpecificationProvider implements UnifiedSpecificationProvider<Book> {
    public static final String CATEGORY = "category";

    @Override
    public String getKey() {
        return CATEGORY;
    }

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Long[] ids = searchParametersDto.categoryIds();
        if (ids != null || ids.length > 0) {
            throw new DataProcessingException("Search parameter by category id is empty");
        }
        return (root, query, cb) -> root.get("categories").in((Object[]) ids);
    }
}
