package pl.kurs.librarybooks.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.kurs.librarybooks.command.CreateBookCommand;
import pl.kurs.librarybooks.dto.BookDTO;
import pl.kurs.librarybooks.dto.GetBookDTO;
import pl.kurs.librarybooks.dto.OverdueBookDTO;
import pl.kurs.librarybooks.dto.StatusDTO;
import pl.kurs.librarybooks.model.Book;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerServiceTest {
    @Mock
    private BookManagementService bookManagementService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookControllerService bookControllerService;

    @Test
    void shouldAddBookAndReturnBookDTO() {
        // given
        CreateBookCommand command = new CreateBookCommand("Title", "Author");
        Book createdBook = new Book(1L, "Title", "Author", null, false, null);
        BookDTO expectedDTO = new BookDTO(1L, "Title", "Author");

        // when
        when(modelMapper.map(command, Book.class)).thenReturn(createdBook);
        when(bookManagementService.add(createdBook)).thenReturn(createdBook);
        when(modelMapper.map(createdBook, BookDTO.class)).thenReturn(expectedDTO);

        BookDTO resultDTO = bookControllerService.addBook(command);

        // then
        assertThat(resultDTO).isEqualTo(expectedDTO);
    }

    @Test
    void shouldGetBookDTOWhenNotOverdue() {
        // given
        long bookId = 1L;
        GetBookDTO expectedDTO = new GetBookDTO(bookId, "Title", "Author", null, false, null);
        Book returnedBook = new Book(bookId, "Title", "Author", null, false, null);

        // when
        when(bookManagementService.getBorrowedDays(bookId)).thenReturn(0);
        when(bookManagementService.get(bookId)).thenReturn(returnedBook);
        when(modelMapper.map(returnedBook, GetBookDTO.class)).thenReturn(expectedDTO);

        // then
        GetBookDTO resultDTO = bookControllerService.getBook(bookId);

        assertThat(resultDTO).isEqualTo(expectedDTO);
    }

    @Test
    void shouldGetOverdueBookDTOWhenOverdue() {
        // given
        long bookId = 2L;
        OverdueBookDTO expectedDTO = new OverdueBookDTO(2L, "Title", "Author", 1L, true, LocalDate.now().minusDays(16), "Book is overdue by 2 days");
        Book returnedbook = new Book(2L, "Title", "Author", 1L, true, LocalDate.now().minusDays(16));
        int overdueDays = 16;

        // when
        when(bookManagementService.getBorrowedDays(bookId)).thenReturn(overdueDays);
        when(bookManagementService.get(bookId)).thenReturn(returnedbook);
        when(modelMapper.map(returnedbook, OverdueBookDTO.class)).thenReturn(expectedDTO);

        // then
        GetBookDTO resultDTO = bookControllerService.getBook(bookId);

        assertThat(resultDTO).isEqualTo(expectedDTO);
    }
    @Test
    void shouldDeleteBookAndReturnOk() {
        // given
        long bookId = 1L;

        // when
        when(bookManagementService.doesBookExist(bookId)).thenReturn(true);

        ResponseEntity<StatusDTO> responseEntity = bookControllerService.deleteBook(bookId);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getStatus()).isEqualTo("Deleted id:" + bookId);
    }

    @Test
    void getBooks() {

    }

    @Test
    void shouldReturnNotFoundForNonexistentBookWhileDeleteBook() {
        // given
        long nonExistentBookId = 2L;

        // when
        when(bookManagementService.doesBookExist(nonExistentBookId)).thenReturn(false);

        ResponseEntity<StatusDTO> responseEntity = bookControllerService.deleteBook(nonExistentBookId);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody().getStatus()).isEqualTo("Book by given id not found");
    }

    @Test
    void updateBook() {
    }

    @Test
    void shouldBorrowBookAndReturnOk() {
        // given
        long bookId = 1L;
        long studentId = 10L;

        when(bookManagementService.isBookByIdBorrowed(bookId)).thenReturn(false);
        when(bookManagementService.get(bookId)).thenReturn(new Book(bookId, "Title", "Author", null, false, null));

        // when
        ResponseEntity<StatusDTO> responseEntity = bookControllerService.borrowBook(bookId, studentId);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getStatus()).isEqualTo("The book with id: 1 was borrowed to a student with id: 10");

        verify(bookManagementService, times(1)).edit(any(Book.class));
    }

    @Test
    void shouldReturnConflictForAlreadyBorrowedBook() {
        // given
        long alreadyBorrowedBookId = 2L;
        long studentId = 10L;

        when(bookManagementService.isBookByIdBorrowed(alreadyBorrowedBookId)).thenReturn(true);

        // when
        ResponseEntity<StatusDTO> responseEntity = bookControllerService.borrowBook(alreadyBorrowedBookId, studentId);

        // then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(responseEntity.getBody().getStatus()).isEqualTo("The book with id: 2 is already borrowed");

        verify(bookManagementService, never()).edit(any(Book.class));
    }
}