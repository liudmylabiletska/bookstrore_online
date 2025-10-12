package mate.academy.bookstoreonline;

import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreonline.model.Book;
import mate.academy.bookstoreonline.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication

public class BookstoreOnlineApplication {

    public static void main(String[] args) {

        SpringApplication.run(BookstoreOnlineApplication.class, args);
    }
}
