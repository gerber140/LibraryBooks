package pl.kurs.librarybooks.service;

import org.springframework.stereotype.Service;
import pl.kurs.librarybooks.exceptions.InvalidIdException;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;


@Service
public class BookManagementService extends GenericManagementService<Book, BookRepository>{
    public BookManagementService(BookRepository repository) {
        super(repository);
    }

    public boolean doesBookExist(long id){
        return repository.existsBookById(id);
    }

    public boolean isBookByIdBorrowed(long id){
            return repository.isBookBorrowed(id);
    }

    public int getBorrowedDays(long id){
        Book book = get(Optional.ofNullable(id).orElseThrow(() -> new InvalidIdException("Invalid id:" + id)));
        if(book.isBorrowed()) {
            Period period = Period.between(book.getBorrowDate(), LocalDate.now());
            return period.getDays();
        }
        else return 0;
    }
}
