package pl.kurs.librarybooks.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OverdueBookDTO extends GetBookDTO{
    private String overdue;

    public OverdueBookDTO(Long id, String title, String author, Long studentId, boolean isBorrowed, LocalDate borrowDate, String overdue) {
        super(id, title, author, studentId, isBorrowed, borrowDate);
        this.overdue = overdue;
    }
}
