package mate.academy.bookstoreonline.repository.book.spec;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.exception.SpecificationNotFoundException;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.repository.book.SpecificationProvider;
import mate.academy.bookstoreonline.repository.book.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManagerImpl implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(
                        () -> new SpecificationNotFoundException(
                                "No specification provider found for key: " + key
                        )
                );
    }
}
