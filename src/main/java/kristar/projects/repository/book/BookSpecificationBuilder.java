package kristar.projects.repository.book;

import java.math.BigDecimal;
import kristar.projects.dto.bookdto.BookSearchParametersDto;
import kristar.projects.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String PRICE = "price";
    public static final String CATEGORY = "category";

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> spec = Specification.anyOf();
        if (searchParametersDto.title() != null
                && searchParametersDto.title().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(TITLE)
                    .getSpecificationString(searchParametersDto.title()));
        }
        if (searchParametersDto.author() != null
                && searchParametersDto.author().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(AUTHOR)
                    .getSpecificationString(searchParametersDto.author()));
        }
        if (searchParametersDto.minPrice() != null && searchParametersDto.maxPrice() != null) {
            if (searchParametersDto.minPrice().compareTo(BigDecimal.ZERO) < 0
                    && searchParametersDto.maxPrice().compareTo(BigDecimal.ZERO) < 0
                    && searchParametersDto.maxPrice().compareTo(searchParametersDto.minPrice()) < 0
            ) {
                throw new IllegalArgumentException("Check min and max prices, it must be positive."
                        + " And Min price cannot be greater than max price");
            }
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(PRICE)
                    .getSpecificationPrice(searchParametersDto.minPrice(),
                            searchParametersDto.maxPrice()));
        }
        if (searchParametersDto.categoryIds() != null
                && searchParametersDto.categoryIds().length > 0) {
            spec = spec.and(bookSpecificationProviderManager.getSpecificationProvider(CATEGORY)
                    .getSpecificationLong(searchParametersDto.categoryIds()));
        }
        return spec;
    }
}
