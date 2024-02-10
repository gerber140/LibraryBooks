package pl.kurs.librarybooks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverdueBookDTO extends GetBookDTO{
    private String overdue;
}
