package kristar.projects.repository.book;

import java.util.List;
import kristar.projects.dto.bookdto.BookSearchParametersDto;
import kristar.projects.model.Book;
import kristar.projects.repository.book.providers.UnifiedSpecificationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final List<UnifiedSpecificationProvider<Book>> providers;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> spec = Specification.anyOf();
        for (UnifiedSpecificationProvider<Book> provider : providers) {
            Specification<Book> partial = provider.build(searchParametersDto);
            if (partial != null) {
                spec = spec.and(partial);
            }
        }
        return spec;
    }
}
