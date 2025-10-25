package mate.academy.bookstoreonline.repository.book;

import mate.academy.bookstoreonline.model.Book;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {

    String getKey();

    Specification<T> getSpecification(String[] params);
}
