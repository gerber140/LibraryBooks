package pl.kurs.librarybooks.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.librarybooks.command.CreateBookCommand;
import pl.kurs.librarybooks.command.UpdateBookCommand;
import pl.kurs.librarybooks.dto.BookDTO;
import pl.kurs.librarybooks.dto.StatusDTO;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.service.BookManagementService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {
    private BookManagementService bookManagementService;
    private ModelMapper modelMapper;

    @PostMapping("/add")
    public ResponseEntity<BookDTO> addBook(@RequestBody CreateBookCommand command){
        Book bookForSave = modelMapper.map(command, Book.class);
        bookForSave.setBorrowed(false);
        bookForSave = bookManagementService.add(bookForSave);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(bookForSave, BookDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable("id") long id){
        return ResponseEntity.ok(modelMapper.map(bookManagementService.get(id), BookDTO.class));
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getBooks(){
        List<BookDTO> response = bookManagementService.getAll().stream()
                .map(x -> modelMapper.map(x, BookDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<BookDTO> updateBookById(@RequestBody UpdateBookCommand command){
        Book bookForUpdate = modelMapper.map(command, Book.class);
        bookForUpdate = bookManagementService.edit(bookForUpdate);
        return ResponseEntity.ok(modelMapper.map(bookForUpdate, BookDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StatusDTO> deleteBookById(@PathVariable("id") long id){
        if(bookManagementService.doesBookExist(id)) {
            bookManagementService.delete(id);
            return ResponseEntity.ok(new StatusDTO("Deleted id:" + id));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatusDTO("Book by given id not found"));
    }

    @PutMapping("/borrowBook/{id_b}/toStudent/{id_s}")
    public ResponseEntity<StatusDTO> borrowBook(@PathVariable("id_b") long idBook, @PathVariable("id_s") long idStudent){
        if(!bookManagementService.isBookByIdBorrowed(idBook)) {
            Book bookToBorrow = bookManagementService.get(idBook);
            bookToBorrow.setBorrowed(true);
            bookToBorrow.setStudentId(idStudent);
            bookToBorrow.setBorrowDate(LocalDate.now());
            bookToBorrow = bookManagementService.edit(bookToBorrow);
            return ResponseEntity.ok(new StatusDTO("The book with id:" + idBook + " was borrowed to a student with id:" + idStudent));
        } else return ResponseEntity.status(HttpStatus.CONFLICT).body(new StatusDTO("The book with id:" + idBook + " is already borrowed"));
    }
}
