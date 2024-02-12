package pl.kurs.librarybooks;

import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import pl.kurs.librarybooks.exceptions.InvalidEntityException;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;
import pl.kurs.librarybooks.service.BookManagementService;
import pl.kurs.librarybooks.service.GenericManagementService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
public class BookManagementServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookManagementService bookManagementService;


    @Test
    void shouldGetBorrowedDays(){
        Book givenBook = new Book(null, "Title", "Author", 1L, true, LocalDate.now().minusDays(5));

        bookManagementService.add(givenBook);

        int borrowedDays = bookManagementService.getBorrowedDays(givenBook.getId());

        assertEquals(5, borrowedDays);
    }

    @Test
    void shouldAddBook(){
        //given
        Book givenBook = new Book(null, "Title", "Author", null, false, null);
        //when
        Book addedBook = bookManagementService.add(givenBook);
        //then
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);

        verify(bookRepository).save(bookArgumentCaptor.capture());

        Book capturedBook = bookArgumentCaptor.getValue();

        assertThat(capturedBook).isEqualTo(givenBook);
    }

    @Test
    void shouldGetBook(){
        //given
        Long bookId = 1L;
        Book expectedBook = new Book(bookId, "Title", "Author", null, false, null);
        //when
        when(bookRepository.findById(eq(bookId))).thenReturn(Optional.of(expectedBook));

        Book retrievedBook = bookManagementService.get(bookId);
        //then
        assertThat(retrievedBook).isEqualTo(expectedBook);
    }
    @Test
    void shouldDeleteBook(){
        Book givenBook = new Book(null, "Title", "Author", null, false, null);
        Book addedBook = bookManagementService.add(givenBook);

        bookManagementService.delete(addedBook.getId());

        verify(bookRepository, times(1)).deleteById(addedBook.getId());
    }



}
