package pl.kurs.librarybooks.controller;

import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "endpoint to add Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Cannot create the book")
    })
    public ResponseEntity<BookDTO> addBook(@RequestBody CreateBookCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookControllerService.addBook(command));
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "endpoint to get book by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book found successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<GetBookDTO> getBookById(@PathVariable("id") long id) {
        return ResponseEntity.ok(bookControllerService.getBook(id));
    }

    @GetMapping("/get")
    @Operation(summary = "endpoint to get books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Books found Successfully"),
            @ApiResponse(responseCode = "404", description = "Books not found")
    })
    public ResponseEntity<List<GetBookDTO>> getBooks(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "value", defaultValue = "id", required = false) String value)
    {
        return ResponseEntity.ok(bookControllerService.getBooks(pageNo, pageSize, value));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "endpoint to delete Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Cannot create the book")
    })
    public ResponseEntity<StatusDTO> deleteBookById(@PathVariable("id") long id) {
        return bookControllerService.deleteBook(id);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "endpoint to update Book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "Book not updated")
    })
    public ResponseEntity<GetBookDTO> updateBookById(@RequestBody UpdateBookCommand command) {
        return ResponseEntity.ok(bookControllerService.updateBook(command));
    }

    @PutMapping("/book/{id_b}/student/{id_s}")
    @Operation(summary = "endpoint to borrow Book to a student")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book borrowed successfully"),
            @ApiResponse(responseCode = "404", description = "Cannot borrow the book")
    })
    public ResponseEntity<StatusDTO> borrowBook(@PathVariable("id_b") long idBook, @PathVariable("id_s") long idStudent) {
        return bookControllerService.borrowBook(idBook, idStudent);
    }
}
