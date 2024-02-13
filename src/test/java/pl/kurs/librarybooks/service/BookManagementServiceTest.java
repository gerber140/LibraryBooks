package pl.kurs.librarybooks.service;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookManagementServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookManagementService bookManagementService;


    @Test
    void shouldGetBorrowedDays(){
        // given
        Book borrowedBook = new Book(1L, "Title", "Author", 1L, true, LocalDate.now().minusDays(5));

        when(bookRepository.findById(borrowedBook.getId())).thenReturn(Optional.of(borrowedBook));

        // when
        int borrowedDays = bookManagementService.getBorrowedDays(borrowedBook.getId());

        // then
        assertThat(borrowedDays).isEqualTo(5);
    }
    @Test
    void shouldReturnZeroForNotBorrowedBook() {
        // given
        Book notBorrowedBook = new Book(2L, "Title", "Author", null, false, null);

        when(bookRepository.findById(notBorrowedBook.getId())).thenReturn(Optional.of(notBorrowedBook));

        // when
        int borrowedDays = bookManagementService.getBorrowedDays(notBorrowedBook.getId());

        // then
        assertThat(borrowedDays).isEqualTo(0);
    }
    @Test
    void shouldAddBook(){
        Book givenBook = new Book(null, "Title", "Author", null, false, null);

        when(bookRepository.save(givenBook)).thenReturn(givenBook);

        Book added = bookManagementService.add(givenBook);

        assertThat(added).isEqualTo(givenBook);
    }

    @Test
    void shouldThrowInvalidEntityExceptionForNullEntityWhileAddBook() {
        assertThatThrownBy(() -> bookManagementService.add(null))
                .isInstanceOf(InvalidEntityException.class)
                .hasMessageContaining("Invalid entity for save");
    }

    @Test
    void shouldGetBook(){
        //given
        Book expectedBook = new Book(1L, "Title", "Author", null, false, null);
        //when
        when(bookRepository.findById(expectedBook.getId())).thenReturn(Optional.of(expectedBook));

        Book retrievedBook = bookManagementService.get(expectedBook.getId());
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
    void shouldGetAllBooks(){
        // given
        List<Book> bookList = List.of(
                new Book(1L, "Title1", "Author1", null, false, null),
                new Book(2L, "Title2", "Author2", null, false, null));

        Page<Book> expectedPage = new PageImpl<>(bookList);

        // when
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        Page<Book> resultPage = bookManagementService.getAll(0, 10, "id");

        // then
        assertThat(resultPage).isEqualTo(expectedPage);
    }

    @Test
    void shouldDeleteBook(){
        // given
        Long bookId = 1L;

        // when
        bookManagementService.delete(bookId);

        // then
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void shouldThrowInvalidIdExceptionForNullIdWhileDeleteBook() {
        assertThatThrownBy(() -> bookManagementService.delete(null))
                .isInstanceOf(InvalidIdException.class)
                .hasMessageContaining("Invalid id: null");
    }

    @Test
    void shouldEditBook(){
        // given
        Book existingBook = new Book(1L, "Title", "Author", null, false, null);

        // when
        when(bookRepository.save(existingBook)).thenReturn(existingBook);

        Book editedBook = bookManagementService.edit(existingBook);

        // then
        assertThat(editedBook).isEqualTo(existingBook);
    }

    @Test
    void shouldThrowInvalidIdExceptionForNullIdWhileEditBook(){
        //when + then
        assertThatThrownBy(() -> bookManagementService.edit(null))
                .isInstanceOf(InvalidEntityException.class)
                .hasMessageContaining("Invalid entity for update");
    }

}
