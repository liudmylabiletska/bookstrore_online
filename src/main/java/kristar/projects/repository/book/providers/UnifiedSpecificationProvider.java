package kristar.projects.repository.book.providers;

import kristar.projects.dto.bookdto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface UnifiedSpecificationProvider<T> {
    String getKey();

    Specification<T> build(BookSearchParametersDto searchParametersDto);
}
