package pl.kurs.librarybooks;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.kurs.librarybooks.security.entity.Role;
import pl.kurs.librarybooks.security.entity.User;
import pl.kurs.librarybooks.security.repository.UserRepository;

import static org.mockito.Mockito.*;

@SpringBootTest
class LibraryBooksApplicationTests {
    @Test
    void contextLoads() {
    }

}
