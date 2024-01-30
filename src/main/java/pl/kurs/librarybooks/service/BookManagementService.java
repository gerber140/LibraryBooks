package pl.kurs.librarybooks.service;

import org.springframework.stereotype.Service;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;


@Service
public class BookManagementService extends GenericManagementService<Book, BookRepository>{
    public BookManagementService(BookRepository repository) {
        super(repository);
    }
}
