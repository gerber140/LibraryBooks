package pl.kurs.librarybooks.service;

import org.junit.jupiter.api.Assertions;
import org.springframework.data.domain.Page;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.kurs.librarybooks.exceptions.EntityNotFoundException;
import pl.kurs.librarybooks.exceptions.InvalidEntityException;
import pl.kurs.librarybooks.exceptions.InvalidIdException;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookManagementServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookManagementService bookManagementService;


    @Test
    void shouldGetBorrowedDays() {
        // given
        long bookId = 1L;
        Book borrowedBook = new Book(bookId, "Title", "Author", 1L, true, LocalDate.now().minusDays(5));

        // when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(borrowedBook));

        int borrowedDays = bookManagementService.getBorrowedDays(bookId);

        // then
        assertThat(borrowedDays).isEqualTo(5);
    }

    @Test
    void shouldReturnZeroForNotBorrowedBook() {
        // given
        long bookId = 2L;
        Book notBorrowedBook = new Book(2L, "Title", "Author", null, false, null);

        // when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(notBorrowedBook));

        int borrowedDays = bookManagementService.getBorrowedDays(bookId);

        // then
        assertThat(borrowedDays).isEqualTo(0);
    }

    @Test
    void shouldAddBook() {
        // given
        Book givenBook = new Book(null, "Title", "Author", null, false, null);

        // when
        when(bookRepository.save(givenBook)).thenReturn(givenBook);

        Book added = bookManagementService.add(givenBook);

        // then
        assertThat(added).isEqualTo(givenBook);
    }

    @Test
    void shouldThrowInvalidEntityExceptionForNullEntityWhileAddBook() {
        assertThatThrownBy(() -> bookManagementService.add(null))
                .isInstanceOf(InvalidEntityException.class)
                .hasMessageContaining("Invalid entity for save");
    }

    @Test
    void shouldGetBook() {
        //given
        long bookId = 1L;
        Book expectedBook = new Book(bookId, "Title", "Author", null, false, null);
        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));

        Book retrievedBook = bookManagementService.get(bookId);
        //then
        assertThat(retrievedBook).isEqualTo(expectedBook);
    }

    @Test
    void shouldThrowInvalidIdExceptionForInvalidIdWhileGetBook() {
        assertThatThrownBy(() -> bookManagementService.get(null))
                .isInstanceOf(InvalidIdException.class)
                .hasMessageContaining("Invalid id: null");
    }

    @Test
    void shouldThrowEntityNotFoundExceptionForNonExistentIdWhileGetBook() {
        // given
        long nonExistentId = 999L;

        //when
        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> bookManagementService.get(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Entity with given id not found:" + nonExistentId);
    }

    @Test
    void shouldGetAllBooks() {
        // given
        int pageNumber = 0;
        int pageSize = 10;
        String value = "id";

        List<Book> bookList = List.of(
                new Book(1L, "Title1", "Author1", null, false, null),
                new Book(2L, "Title2", "Author2", null, false, null),
                new Book(3L, "Title3", "Author3", 1L, true, LocalDate.now())
        );

        Page<Book> expectedPage = new PageImpl<>(bookList);

        // when
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Book> resultPage = bookManagementService.getAll(pageNumber, pageSize, value);

        // then
        assertThat(resultPage).isEqualTo(expectedPage);
    }

    @Test
    void shouldDeleteBook() {
        // given
        Long bookId = 1L;

        // when
        bookManagementService.delete(bookId);

        ArgumentCaptor<Long> idCaptor = ArgumentCaptor.forClass(Long.class);
        verify(bookRepository).deleteById(idCaptor.capture());

        // then
        assertEquals(bookId, idCaptor.getValue());

    }

    @Test
    void shouldThrowInvalidIdExceptionForNullIdWhileDeleteBook() {
        assertThatThrownBy(() -> bookManagementService.delete(null))
                .isInstanceOf(InvalidIdException.class)
                .hasMessageContaining("Invalid id: null");
    }

    @Test
    void shouldEditBook() {
        // given
        Book existingBook = new Book(1L, "Title", "Author", null, false, null);

        // when
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book editedBook = bookManagementService.edit(existingBook);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());

        Book capturedBook = bookCaptor.getValue();

        assertEquals(existingBook, capturedBook);
        assertEquals(existingBook, editedBook);
    }

    @Test
    void shouldThrowInvalidIdExceptionForNullIdWhileEditBook() {
        //when + then
        assertThatThrownBy(() -> bookManagementService.edit(null))
                .isInstanceOf(InvalidEntityException.class)
                .hasMessageContaining("Invalid entity for update");
    }

}
