package kristar.projects.repository.book.providers;

import java.util.Arrays;
import kristar.projects.model.Book;
import kristar.projects.repository.SpecificationProvider;
import kristar.projects.repository.book.BookSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public abstract class AuthorSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public Specification<Book> getSpecificationString(String[] params) {

        return (root, query, criteriaBuilder)
                -> root.get(BookSpecificationBuilder.AUTHOR).in(Arrays.stream(params).toArray());
    }

    @Override
    public String getKey() {
        return BookSpecificationBuilder.AUTHOR;
    }
}
