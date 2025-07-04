package kristar.projects.repository.book.providers;

import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.List;
import kristar.projects.dto.book.BookSearchParametersDto;
import kristar.projects.exception.DataProcessingException;
import kristar.projects.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("title")
public class TitleSpecificationProvider implements UnifiedSpecificationProvider<Book> {
    public static final String TITLE_FIELD = "title";

    @Override
    public String getKey() {
        return TITLE_FIELD;
    }

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        if (searchParametersDto.titles() == null || searchParametersDto.titles().length == 0) {
            throw new DataProcessingException("Search parameter by title is empty");
        }
        String[] titles = searchParametersDto.titles();

        return (root, query, cb) -> {
            List<Predicate> predicates = Arrays.stream(titles)
                    .filter(title -> title != null && !title.isBlank())
                    .map(title -> cb.like(cb.lower(root.get(TITLE_FIELD)),
                            "%" + title.toLowerCase().trim() + "%"))
                    .toList();

            return predicates.isEmpty() ? null : cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
