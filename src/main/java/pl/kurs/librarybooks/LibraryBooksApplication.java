package pl.kurs.librarybooks;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.kurs.librarybooks.dto.BookDTO;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;
import pl.kurs.librarybooks.security.entity.Role;
import pl.kurs.librarybooks.security.entity.User;
import pl.kurs.librarybooks.security.repository.UserRepository;
import pl.kurs.librarybooks.service.BookManagementService;

@SpringBootApplication
public class LibraryBooksApplication {
    @Autowired
    private UserRepository repository;
    public static void main(String[] args) {
        SpringApplication.run(LibraryBooksApplication.class, args);
    }

}
