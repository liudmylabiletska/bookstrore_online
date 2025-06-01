package kristar.projects;

import java.math.BigDecimal;

import kristar.projects.dto.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import kristar.projects.model.Book;
import kristar.projects.service.BookService;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreApplication.class, args);
    }
    //    @Bean
    //    public CommandLineRunner commandLineRunner(BookService bookService) {
    //        return args -> {
    //            BookDto book1 = new BookDto();
    //            book1.setTitle("One Hundred Years of Solitude");
    //            book1.setAuthor("Gabriel Garcia Marquez");
    //            book1.setIsbn("san-123-45-02t");
    //            book1.setPrice(BigDecimal.valueOf(499));
    //            bookService.save(book1);
    //
    //            Book book2 = new Book();
    //            book2.setTitle("Arch of Triumph");
    //            book2.setAuthor("Erich Maria Remarque");
    //            book2.setIsbn("san-126-85-02t");
    //            book2.setPrice(BigDecimal.valueOf(900));
    //            bookService.save(book2);
    //
    //            System.out.println(bookService.getAll());
    //        };
    //    }
}
