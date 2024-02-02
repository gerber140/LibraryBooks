package pl.kurs.librarybooks.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
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
import pl.kurs.librarybooks.service.BookManagementService;

import java.util.List;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {
    private BookManagementService bookManagementService;
    private BookControllerService bookControllerService;
    private ModelMapper modelMapper;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<BookDTO> addBook(@RequestBody CreateBookCommand command){
        return ResponseEntity.status(HttpStatus.CREATED).body(bookControllerService.addBook(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetBookDTO> getBookById(@PathVariable("id") long id){
        return ResponseEntity.ok(bookControllerService.getBook(id));
    }

    @GetMapping
    public ResponseEntity<List<GetBookDTO>> getBooks(){
        return ResponseEntity.ok(bookControllerService.getBooks());
    }


    @PutMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<GetBookDTO> updateBookById(@RequestBody UpdateBookCommand command){
        return ResponseEntity.ok(bookControllerService.updateBook(command));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StatusDTO> deleteBookById(@PathVariable("id") long id){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(bookControllerService.deleteBook(id));
    }

    @PutMapping("/borrowBook/{id_b}/toStudent/{id_s}")
    public ResponseEntity<StatusDTO> borrowBook(@PathVariable("id_b") long idBook, @PathVariable("id_s") long idStudent){
        if(!bookControllerService.isBookBorrowed(idBook)) {
            return ResponseEntity.ok(bookControllerService.borrowBook(idBook, idStudent));
        } else return ResponseEntity.status(HttpStatus.CONFLICT).body(new StatusDTO("The book with id:" + idBook + " is already borrowed"));

    }
}
