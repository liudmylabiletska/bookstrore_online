package kristar.projects.repository.book.providers;

import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.List;
import kristar.projects.dto.book.BookSearchParametersDto;
import kristar.projects.exception.DataProcessingException;
import kristar.projects.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("author")
public class AuthorSpecificationProvider
        implements UnifiedSpecificationProvider<Book> {
    public static final String AUTHOR_FIELD = "author";

    @Override
    public String getKey() {
        return AUTHOR_FIELD;
    }

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        if (searchParametersDto.authors() == null || searchParametersDto.authors().length == 0) {
            throw new DataProcessingException("Search parameter by authors is empty");
        }
        String[] authors = searchParametersDto.authors();

        return (root, query, cb) -> {
            List<Predicate> predicates = Arrays.stream(authors)
                    .filter(author -> author != null && !author.isBlank())
                    .map(author -> cb.like(cb.lower(root.get(AUTHOR_FIELD)),
                            "%" + author.toLowerCase().trim() + "%"))
                    .toList();

            return predicates.isEmpty() ? null : cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
