package pl.kurs.librarybooks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.stereotype.Repository;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
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
