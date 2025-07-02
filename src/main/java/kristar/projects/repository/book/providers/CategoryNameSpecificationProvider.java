package kristar.projects.repository.book.providers;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.List;
import kristar.projects.dto.bookdto.BookSearchParametersDto;
import kristar.projects.exception.DataProcessingException;
import kristar.projects.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("categoryNames")
public class CategoryNameSpecificationProvider implements UnifiedSpecificationProvider<Book> {
    public static final String CATEGORY_NAMES = "categoryNames";
    public static final String CATEGORIES_TABLE = "categories";
    public static final String NAME_CATEGORIES_TABLE = "name";

    @Override
    public String getKey() {
        return CATEGORY_NAMES;
    }

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        String[] categoryNames = searchParametersDto.categoryNames();
        if (categoryNames == null || categoryNames.length == 0) {
            throw new DataProcessingException("Search parameter by categoryNames is empty");
        }

        return (root, query, cb) -> {
            Join<Object, Object> join = root.join(CATEGORIES_TABLE);

            List<Predicate> predicates = Arrays.stream(categoryNames)
                    .filter(name -> name != null && !name.isBlank())
                    .map(name -> cb.like(cb.lower(join.get(NAME_CATEGORIES_TABLE)),
                    "%" + name.toLowerCase().trim() + "%"
                ))
                    .toList();

            return predicates.isEmpty() ? null : cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
