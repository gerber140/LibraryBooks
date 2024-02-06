package pl.kurs.librarybooks.repository;

import com.querydsl.core.types.dsl.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.model.QBook;
import pl.kurs.librarybooks.security.entity.QUser;

import java.util.Date;
import java.util.List;


public interface BookRepository extends JpaRepository<Book, Long>, QuerydslPredicateExecutor<Book>, QuerydslBinderCustomizer<QBook> {
    boolean existsBookById(long id);

    @Query("SELECT b.isBorrowed FROM Book b WHERE b.id = :id")
    boolean isBookBorrowed(long id);

    @Override
    default public void customize(QuerydslBindings bindings, QBook book) {
        bindings.bind(book.title)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);

        bindings.bind(book.author)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);

        bindings.bind(book.isBorrowed)
                .first((SingleValueBinding<BooleanPath, Boolean>) BooleanExpression::eq);
    }
}
