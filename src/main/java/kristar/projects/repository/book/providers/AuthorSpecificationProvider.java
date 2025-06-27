package kristar.projects.repository.book.providers;

import kristar.projects.dto.bookdto.BookSearchParametersDto;
import kristar.projects.exception.DataProcessingException;
import kristar.projects.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("author")
public class AuthorSpecificationProvider
        implements UnifiedSpecificationProvider<Book> {
    public static final String AUTHOR = "author";

    @Override
    public String getKey() {
        return AUTHOR;
    }

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        if (searchParametersDto.author() == null || searchParametersDto.author().length == 0) {
            throw new DataProcessingException("Search parameter by author is empty");
        }
        return (root, query, cb) -> root.get(AUTHOR)
                .in((Object[]) searchParametersDto.author());
    }
}
