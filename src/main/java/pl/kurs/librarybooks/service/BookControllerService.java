package pl.kurs.librarybooks.service;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.kurs.librarybooks.command.CreateBookCommand;
import pl.kurs.librarybooks.command.UpdateBookCommand;
import pl.kurs.librarybooks.dto.BookDTO;
import pl.kurs.librarybooks.dto.GetBookDTO;
import pl.kurs.librarybooks.dto.OverdueBookDTO;
import pl.kurs.librarybooks.dto.StatusDTO;
import pl.kurs.librarybooks.model.Book;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class BookControllerService {
    private BookManagementService bookManagementService;
    private ModelMapper modelMapper;
    private static final int OVERDUE_DAYS = 14;

    public BookDTO addBook(CreateBookCommand command) {
        return modelMapper.map(bookManagementService.add(modelMapper.map(command, Book.class)), BookDTO.class);
    }

    public GetBookDTO getBook(long id) {
        if(bookManagementService.getBorrowedDays(id) >= OVERDUE_DAYS){
            OverdueBookDTO response = modelMapper.map(bookManagementService.get(id), OverdueBookDTO.class);
            response.setOverdue("Book is overdue by " + (bookManagementService.getBorrowedDays(id) - OVERDUE_DAYS) + " days");
            return response;
        } else
            return modelMapper.map(bookManagementService.get(id), GetBookDTO.class);
    }

    public List<GetBookDTO> getBooks(int page, int size, String value) {
        return bookManagementService.getAll(page, size, value)
                .getContent()
                .stream()
                .map(x -> getBook(x.getId()))
                .toList();
    }

    public ResponseEntity<StatusDTO> deleteBook(long id) {
        if (bookManagementService.doesBookExist(id)) {
            bookManagementService.delete(id);
            return ResponseEntity.ok(new StatusDTO("Deleted id:" + id));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatusDTO("Book by given id not found"));
    }

    public GetBookDTO updateBook(UpdateBookCommand command) {
        Book bookForUpdate = modelMapper.map(command, Book.class);
        bookForUpdate = bookManagementService.edit(bookForUpdate);
        return modelMapper.map(bookForUpdate, GetBookDTO.class);
    }

    public ResponseEntity<StatusDTO> borrowBook(long idBook, long idStudent) {
        if (!bookManagementService.isBookByIdBorrowed(idBook)) {
            Book bookToBorrow = bookManagementService.get(idBook);
            bookToBorrow.setBorrowed(true);
            bookToBorrow.setStudentId(idStudent);
            bookToBorrow.setBorrowDate(LocalDate.now());
            bookManagementService.edit(bookToBorrow);
            return ResponseEntity.ok(new StatusDTO("The book with id: " + idBook + " was borrowed to a student with id: " + idStudent));
        } else
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new StatusDTO("The book with id: " + idBook + " is already borrowed"));
    }
}
