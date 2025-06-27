package kristar.projects.repository.book.providers;

import jakarta.persistence.criteria.Predicate;
import java.util.Arrays;
import java.util.List;
import kristar.projects.dto.bookdto.BookSearchParametersDto;
import kristar.projects.exception.DataProcessingException;
import kristar.projects.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("title")
public class TitleSpecificationProvider implements UnifiedSpecificationProvider<Book> {
    public static final String TITLE = "title";

    @Override
    public String getKey() {
        return TITLE;
    }

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        if (searchParametersDto.title() == null || searchParametersDto.title().length == 0) {
            throw new DataProcessingException("Search parameter by title is empty");
        }
        String[] titles = searchParametersDto.author();

        return (root, query, cb) -> {
            List<Predicate> predicates = Arrays.stream(titles)
                    .filter(author -> author != null && !author.isBlank())
                    .map(author -> cb.like(cb.lower(root.get("title")),
                            "%" + author.toLowerCase().trim() + "%"))
                    .toList();

            return predicates.isEmpty() ? null : cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
