package kristar.projects.repository.book.providers;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Arrays;
import kristar.projects.model.Book;
import kristar.projects.repository.SpecificationProvider;
import kristar.projects.repository.book.BookSpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public abstract class TitleSpecificationProvider implements SpecificationProvider<Book> {

    @Override
    public Specification<Book> getSpecificationString(String[] params) {

        return new Specification<Book>() {
            @Override
            public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query,
                                         CriteriaBuilder criteriaBuilder) {
                return root.get(BookSpecificationBuilder.TITLE).in(Arrays.stream(params).toArray());
            }
        };
    }

    @Override
    public String getKey() {
        return BookSpecificationBuilder.TITLE;
    }
}
