package pl.kurs.librarybooks;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;
import pl.kurs.librarybooks.service.BookManagementService;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookManagementServiceTest {
    @Autowired
    private BookManagementService bookManagementService;

    @Test
    void shouldGetBorrowedDays(){
        Book givenBook = new Book(null, "Title", "Author", 1L, true, LocalDate.now().minusDays(5));

        bookManagementService.add(givenBook);

        int borrowedDays = bookManagementService.getBorrowedDays(givenBook.getId());

        assertEquals(5, borrowedDays);
    }



}
