package pl.kurs.librarybooks.controller;

import com.querydsl.core.types.dsl.BooleanExpression;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.kurs.librarybooks.BookPredicatesBuilder;
import pl.kurs.librarybooks.command.CreateBookCommand;
import pl.kurs.librarybooks.command.UpdateBookCommand;
import pl.kurs.librarybooks.dto.BookDTO;
import pl.kurs.librarybooks.dto.GetBookDTO;
import pl.kurs.librarybooks.dto.StatusDTO;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;
import pl.kurs.librarybooks.service.BookControllerService;
import pl.kurs.librarybooks.service.BookManagementService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {
    private BookControllerService bookControllerService;

    @GetMapping( value = "/getBooks")
    public Iterable<Book> search(@RequestParam(value = "search") String search) {
        BookPredicatesBuilder builder = new BookPredicatesBuilder();

        if (search != null) {
            Pattern pattern = Pattern.compile("(\w+?)(:|<|>)(\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        BooleanExpression exp = builder.build();
        return BookRepository.findAll(exp);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "endpoint to add Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully", content =
                    {@Content(mediaType = "application/json", schema =
                    @Schema(implementation = BookDTO.class))})
    })
    public ResponseEntity<BookDTO> addBook(@RequestBody CreateBookCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookControllerService.addBook(command));
    }

    @GetMapping("/getBook/{id}")
    @Operation(summary = "endpoint to get Book by id")
    public ResponseEntity<GetBookDTO> getBookById(@PathVariable("id") long id) {
        return ResponseEntity.ok(bookControllerService.getBook(id));
    }

    @GetMapping("/getBooks/pageNo/{pageNo}/pageSize/{pageSize}")
    public ResponseEntity<List<GetBookDTO>> getBooks(@PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize) {
        return ResponseEntity.ok(bookControllerService.getBooks(pageNo, pageSize));
    }


    @GetMapping
    @Operation(summary = "endpoint to get all Books")
    public ResponseEntity<List<GetBookDTO>> getBooks() {
        return ResponseEntity.ok(bookControllerService.getBooks());
    }


    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "endpoint to update Book")
    public ResponseEntity<GetBookDTO> updateBookById(@RequestBody UpdateBookCommand command) {
        return ResponseEntity.ok(bookControllerService.updateBook(command));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "endpoint to delete Book")
    public ResponseEntity<StatusDTO> deleteBookById(@PathVariable("id") long id) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookControllerService.deleteBook(id));
    }

    @PutMapping("/borrowBook/{id_b}/toStudent/{id_s}")
    @Operation(summary = "endpoint to borrow Book to a student")
    public ResponseEntity<StatusDTO> borrowBook(@PathVariable("id_b") long idBook, @PathVariable("id_s") long idStudent) {
        if (!bookControllerService.isBookBorrowed(idBook)) {
            return ResponseEntity.ok(bookControllerService.borrowBook(idBook, idStudent));
        } else
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new StatusDTO("The book with id:" + idBook + " is already borrowed"));
    }
}
