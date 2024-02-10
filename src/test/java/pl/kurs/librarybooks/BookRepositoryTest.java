package pl.kurs.librarybooks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookRepositoryTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private BookRepository bookRepository;

    @Test
    void shouldCheckIfBookByIdExist(){
        Book givenBook = new Book(1L,"Title", "Author", null, false, null);

        bookRepository.save(givenBook);

        boolean exists = bookRepository.existsBookById(1L);

        assertTrue(exists);
    }

    @Test
    void shouldCheckIfBookIsBorrowed(){
        Book givenBook = new Book(1L,"Title", "Author", null, true, null);

        bookRepository.save(givenBook);

        boolean bookBorrowed = bookRepository.isBookBorrowed(givenBook.getId());

        assertTrue(bookBorrowed);
    }


    @Test
    void shouldAddBook(){
        //given
        Book givenBook = new Book(null, "Title", "Author", null, false, null);
        //when
        Book addedBook = bookRepository.save(givenBook);

        //then
        assertAll(
                () -> assertEquals(addedBook.getId(), givenBook.getId()),
                () -> assertEquals(addedBook.getTitle(), givenBook.getTitle()),
                () -> assertEquals(addedBook.getAuthor(), givenBook.getAuthor()),
                () -> assertEquals(addedBook.getStudentId(), givenBook.getStudentId()),
                () -> assertEquals(addedBook.isBorrowed(), givenBook.isBorrowed()),
                () -> assertEquals(addedBook.getBorrowDate(), givenBook.getBorrowDate())
        );
    }


}
