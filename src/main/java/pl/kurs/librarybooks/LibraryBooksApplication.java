package pl.kurs.librarybooks;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.kurs.librarybooks.dto.BookDTO;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;
import pl.kurs.librarybooks.security.entity.Role;
import pl.kurs.librarybooks.security.entity.User;
import pl.kurs.librarybooks.security.repository.UserRepository;
import pl.kurs.librarybooks.service.BookManagementService;

@SpringBootApplication
public class LibraryBooksApplication implements CommandLineRunner{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    public static void main(String[] args) {
        SpringApplication.run(LibraryBooksApplication.class, args);
    }

    @Override
    public void run(String... args) {
        createAdminIfNotExists();
    }

    private void createAdminIfNotExists() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
        }
    }
}
