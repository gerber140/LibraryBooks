package pl.kurs.librarybooks;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.kurs.librarybooks.dto.BookDTO;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;
import pl.kurs.librarybooks.service.BookManagementService;

@SpringBootApplication
public class LibraryBooksApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryBooksApplication.class, args);

    }

}
