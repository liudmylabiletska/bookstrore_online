package kristar.projects.controller;

import kristar.projects.dto.BookDto;
import kristar.projects.dto.CreateBookRequestDto;
import kristar.projects.model.Book;
import kristar.projects.service.BookService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/books")
public class BookController  {

    private final BookService bookService;

    @GetMapping
    public List<BookDto> findAll() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.finById(id);
    }

    @PostMapping
    public BookDto createBook(@RequestBody CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }
}
