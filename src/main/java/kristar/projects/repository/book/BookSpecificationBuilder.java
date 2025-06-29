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
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.anyOf();

        for (UnifiedSpecificationProvider<Book> provider : providers) {
            try {
                Specification<Book> partial = provider.build(searchParameters);
                if (partial != null) {
                    System.out.println("✅ Provider [" + provider.getKey() + "] applied.");
                    spec = spec.and(partial);
                } else {
                    System.out.println("⏭ Provider [" + provider.getKey()
                            + "] skipped (null spec).");
                }
            } catch (Exception e) {
                System.err.println("❌ Provider [" + provider.getKey()
                        + "] failed: " + e.getMessage());
            }
        }
        return spec;
    }
}
