package pl.kurs.librarybooks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.librarybooks.model.Book;


public interface BookRepository extends JpaRepository<Book, Long> {
}
