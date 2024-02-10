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

    public BookDTO addBook(CreateBookCommand command) {
        Book bookForSave = modelMapper.map(command, Book.class);
        bookForSave.setBorrowed(false);
        bookForSave = bookManagementService.add(bookForSave);
        return modelMapper.map(bookForSave, BookDTO.class);
    }

    public GetBookDTO getBook(long id) {
        if(bookManagementService.getBorrowedDays(id) >= 14){
            OverdueBookDTO response = modelMapper.map(bookManagementService.get(id), OverdueBookDTO.class);
            response.setOverdue("Book is overdue by " + (bookManagementService.getBorrowedDays(id) - 14) + " days");
            return response;
        } else
            return modelMapper.map(bookManagementService.get(id), GetBookDTO.class);
    }

    public List<GetBookDTO> getBooks(int page, int size, String value) {
        Page<Book> booksPage = bookManagementService.getAll(page, size, value);
        List<Book> booksList = booksPage.getContent();

        return booksList.stream().map(
                x -> getBook(x.getId())
        ).toList();
    }

    public GetBookDTO updateBook(UpdateBookCommand command) {
        Book bookForUpdate = modelMapper.map(command, Book.class);
        bookForUpdate = bookManagementService.edit(bookForUpdate);
        return modelMapper.map(bookForUpdate, GetBookDTO.class);
    }

    public ResponseEntity<StatusDTO> deleteBook(long id) {
        if (bookManagementService.doesBookExist(id)) {
            bookManagementService.delete(id);
            return ResponseEntity.ok(new StatusDTO("Deleted id:" + id));
        } else return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new StatusDTO("Book by given id not found"));
    }

    public ResponseEntity<StatusDTO> borrowBook(long idBook, long idStudent) {
        if (!bookManagementService.isBookByIdBorrowed(idBook)) {
            Book bookToBorrow = bookManagementService.get(idBook);
            bookToBorrow.setBorrowed(true);
            bookToBorrow.setStudentId(idStudent);
            bookToBorrow.setBorrowDate(LocalDate.now());
            bookManagementService.edit(bookToBorrow);
            return ResponseEntity.ok(new StatusDTO("The book with id:" + idBook + " was borrowed to a student with id:" + idStudent));
        } else
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new StatusDTO("The book with id:" + idBook + " is already borrowed"));
    }
}
