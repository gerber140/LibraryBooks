package pl.kurs.librarybooks.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class GetBookDTO {
    private Long id;
    private String title;
    private String author;
    private Long studentId;
    private boolean isBorrowed;
    private LocalDate borrowDate;
}
