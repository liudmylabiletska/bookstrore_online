package mate.academy.bookstoreonline.controller;

import mate.academy.bookstoreonline.dto.BookDto;
import mate.academy.bookstoreonline.dto.BookSearchParametersDto;
import mate.academy.bookstoreonline.dto.CreateBookRequestDto;
import mate.academy.bookstoreonline.dto.UpdateBookRequestDto;
import mate.academy.bookstoreonline.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;


    @GetMapping
    public List<BookDto> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookDto save(@RequestBody @Valid CreateBookRequestDto requestBook) {
        return bookService.save(requestBook);
    }

    @PutMapping("/{id}")
    public BookDto updateBook(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBookRequestDto requestBook) {
        return bookService.update(id, requestBook);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.delete(id);
    }

    @GetMapping("/search")
    public List<BookDto> searchBooks(@Valid BookSearchParametersDto searchParameters) {
        return bookService.search(searchParameters);
    }
}
