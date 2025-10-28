package mate.academy.bookstoreonline.service;

import mate.academy.bookstoreonline.dto.CreateBookRequestDto;
import mate.academy.bookstoreonline.dto.BookDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    BookDto register(CreateBookRequestDto requestDto);
}
