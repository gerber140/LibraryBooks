package pl.kurs.librarybooks.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.kurs.librarybooks.model.Book;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsBookById(long id);

    @Query("SELECT b.isBorrowed FROM Book b WHERE b.id = :id")
    boolean isBookBorrowed(long id);
}
