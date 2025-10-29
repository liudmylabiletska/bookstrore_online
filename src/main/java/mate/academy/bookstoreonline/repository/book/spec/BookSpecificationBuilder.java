package mate.academy.bookstoreonline.repository.book.spec;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.dto.BookSearchParametersDto;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.repository.book.SpecificationBuilder;
import mate.academy.bookstoreonline.repository.book.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR_KEY = "author";
    private static final String TITLE_KEY = "title";
    private static final String ISBN_KEY = "isbn";
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto bookSearchParametersDto) {
        Specification<Book> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (bookSearchParametersDto.authors() != null
                && bookSearchParametersDto.authors().length > 0) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(bookSearchParametersDto.authors()));
        }
        if (bookSearchParametersDto.titles() != null
                && bookSearchParametersDto.titles().length > 0) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider(TITLE_KEY)
                    .getSpecification(bookSearchParametersDto.titles()));
        }
        if (bookSearchParametersDto.isbns() != null && bookSearchParametersDto.isbns().length > 0) {
            specification = specification.and(specificationProviderManager
                    .getSpecificationProvider(ISBN_KEY)
                    .getSpecification(bookSearchParametersDto.isbns()));
        }
        return specification;
    }
}
