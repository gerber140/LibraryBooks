package pl.kurs.librarybooks.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.kurs.librarybooks.command.CreateBookCommand;
import pl.kurs.librarybooks.command.UpdateBookCommand;
import pl.kurs.librarybooks.dto.BookDTO;
import pl.kurs.librarybooks.dto.GetBookDTO;
import pl.kurs.librarybooks.dto.StatusDTO;
import pl.kurs.librarybooks.service.BookControllerService;

import java.util.List;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {
    private BookControllerService bookControllerService;

    @GetMapping("/getBooks")
    @ResponseBody
    @Operation(summary = "Search books based on criteria")
    public ResponseEntity<List<GetBookDTO>> search(@RequestParam(value = "search") String search) {
        return ResponseEntity.ok(bookControllerService.search(search));
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
