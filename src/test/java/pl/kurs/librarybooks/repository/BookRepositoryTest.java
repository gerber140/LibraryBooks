package pl.kurs.librarybooks.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.kurs.librarybooks.model.Book;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @AfterEach
    void tearDown(){
        bookRepository.deleteAll();
    }


    @Test
    void shouldCheckIfBookByIdExist(){
        //given
        Book givenBook = new Book(null,"Title", "Author", null, false, null);
        //when
        bookRepository.save(givenBook);

        boolean exists = bookRepository.existsBookById(givenBook.getId());
        //then
        assertTrue(exists);
        assertThat(givenBook).isNotNull();
        assertThat(givenBook.getId()).isGreaterThan(0);
    }

    @Test
    void shouldCheckIfBookByIdNotExist(){
        //when
        boolean exists = bookRepository.existsBookById(1L);
        //then
        assertFalse(exists);
    }

    @Test
    void shouldCheckIfBookIsBorrowed(){
        //given
        Book givenBook = new Book(null,"Title", "Author", 1L, true, LocalDate.now());
        //when
        bookRepository.save(givenBook);

        boolean bookBorrowed = bookRepository.isBookBorrowed(givenBook.getId());
        //then
        assertTrue(bookBorrowed);
    }

    @Test
    void shouldCheckIfBookIsNotBorrowed(){
        //given
        Book givenBook = new Book(null,"Title", "Author", null, false, null);
        //when
        bookRepository.save(givenBook);

        boolean bookBorrowed = bookRepository.isBookBorrowed(givenBook.getId());
        //then
        assertFalse(bookBorrowed);
    }

}
