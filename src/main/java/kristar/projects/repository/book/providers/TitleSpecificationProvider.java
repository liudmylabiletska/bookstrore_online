package kristar.projects.repository.book.providers;

import kristar.projects.dto.bookdto.BookSearchParametersDto;
import kristar.projects.exception.DataProcessingException;
import kristar.projects.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("title")
public class TitleSpecificationProvider implements UnifiedSpecificationProvider<Book> {
    public static final String TITLE = "title";

    @Override
    public String getKey() {
        return TITLE;
    }

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        if (searchParametersDto.title() == null || searchParametersDto.title().length == 0) {
            throw new DataProcessingException("Search parameter by title is empty");
        }
        return (root, query, cb) -> root.get(TITLE)
                .in((Object[]) searchParametersDto.title());
    }
}
