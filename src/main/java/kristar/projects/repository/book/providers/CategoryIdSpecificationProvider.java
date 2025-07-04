package kristar.projects.repository.book.providers;

import jakarta.persistence.criteria.Join;
import kristar.projects.dto.book.BookSearchParametersDto;
import kristar.projects.exception.DataProcessingException;
import kristar.projects.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("categoryIds")
public class CategoryIdSpecificationProvider implements UnifiedSpecificationProvider<Book> {
    public static final String CATEGORY = "categoryIds";
    public static final String CATEGORIES_TABLE = "categories";
    public static final String ID_CATEGORIES_TABLE = "id";

    @Override
    public String getKey() {
        return CATEGORY;
    }

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Long[] categoryIds = searchParametersDto.categoryIds();
        if (categoryIds == null || categoryIds.length == 0) {
            throw new DataProcessingException("Search parameter by categoryIds id is empty");
        }
        return (root, query, cb) -> {
            Join<Object, Object> join = root.join(CATEGORIES_TABLE);

            return join.get(ID_CATEGORIES_TABLE).in((Object[]) categoryIds);
        };
    }
}
