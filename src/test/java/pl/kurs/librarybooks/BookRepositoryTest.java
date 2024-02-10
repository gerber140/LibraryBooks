package pl.kurs.librarybooks;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookRepositoryTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void tearDown(){
        bookRepository.deleteAll();
    }

    @Test
    void shouldCheckIfBookByIdExist(){
        Book givenBook = new Book(null,"Title", "Author", null, false, null);

        bookRepository.save(givenBook);

        boolean exists = bookRepository.existsBookById(givenBook.getId());

        assertTrue(exists);
    }

    @Test
    void shouldCheckIfBookByIdNotExist(){
        boolean exists = bookRepository.existsBookById(1L);

        assertFalse(exists);
    }

    @Test
    void shouldCheckIfBookIsBorrowed(){
        Book givenBook = new Book(null,"Title", "Author", 1L, true, LocalDate.now());

        bookRepository.save(givenBook);

        boolean bookBorrowed = bookRepository.isBookBorrowed(givenBook.getId());

        assertTrue(bookBorrowed);
    }

    @Test
    void shouldCheckIfBookIsNotBorrowed(){
        Book givenBook = new Book(null,"Title", "Author", null, false, null);

        bookRepository.save(givenBook);

        boolean bookBorrowed = bookRepository.isBookBorrowed(givenBook.getId());

        assertFalse(bookBorrowed);
    }

}
