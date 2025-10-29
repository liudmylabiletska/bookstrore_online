package mate.academy.bookstoreonline.repository.book.spec;

import java.util.Arrays;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.repository.book.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String TITLE_FIELD = "title";

    @Override
    public String getKey() {
        return TITLE_FIELD;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(TITLE_FIELD)
                .in(Arrays.stream(params).toArray());
    }
}
