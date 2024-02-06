package pl.kurs.librarybooks.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Service;
import pl.kurs.librarybooks.querydsl.BookPredicatesBuilder;
import pl.kurs.librarybooks.model.Book;
import pl.kurs.librarybooks.repository.BookRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class BookManagementService extends GenericManagementService<Book, BookRepository>{
    public BookManagementService(BookRepository repository) {
        super(repository);
    }

    public boolean doesBookExist(long id){
        return repository.existsBookById(id);
    }

    public boolean isBookByIdBorrowed(long id){
            return repository.isBookBorrowed(id);
    }

    public long getBorrowedDays(Book book){
        LocalDate borrowDate = book.getBorrowDate();
        Period period = Period.between(borrowDate, LocalDate.now());
        return period.getDays();
    }
    public List<Book> search(String search) {
        BookPredicatesBuilder builder = new BookPredicatesBuilder();

        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)([:<>])(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        BooleanExpression exp = builder.build();
        Iterable<Book> booksIterable = repository.findAll(exp);
        List<Book> result = new ArrayList<>();
        booksIterable.forEach(result::add);
        return result;
    }
}
