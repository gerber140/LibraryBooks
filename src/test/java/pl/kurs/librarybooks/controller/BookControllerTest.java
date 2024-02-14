package pl.kurs.librarybooks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kurs.librarybooks.LibraryBooksApplication;
import pl.kurs.librarybooks.command.CreateBookCommand;
import pl.kurs.librarybooks.dto.BookDTO;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;
import pl.kurs.librarybooks.service.BookControllerService;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = LibraryBooksApplication.class)

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc postman;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private BookControllerService bookControllerService;

    @Test
    public void shouldAddBook() throws Exception {
        String title = "Title";
        String author = "Author";
        CreateBookCommand command = new CreateBookCommand(title, author);
        BookDTO expectedBook = new BookDTO(1L, "Title", "Author");

        when(bookControllerService.addBook(command)).thenReturn(expectedBook);

        postman.perform(post("/books/add")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Book savedBook = bookControllerService.getBook(1L);

        assertAll(
                () -> assertEquals(savedBook.getTitle(), command.getTitle()),
                () -> assertEquals(savedBook.getId(), 1L),
                () -> assertEquals(savedBook.getAuthor(), command.getAuthor())
        );
    }

}