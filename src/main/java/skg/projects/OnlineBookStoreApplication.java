package skg.projects;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import skg.projects.model.Book;
import skg.projects.service.BookService;

@SpringBootApplication
public class OnlineBookStoreApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }

    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("One Hundred Years of Solitude");
            book.setAuthor("Gabriel Garcia Marquez");
            book.setIsbn("12345SAN");
            book.setPrice(BigDecimal.valueOf(499));

            bookService.save(book);

            System.out.println(bookService.getAll());
        };
    }
}
