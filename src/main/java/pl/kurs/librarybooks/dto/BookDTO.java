package pl.kurs.librarybooks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class BookDTO {
    private Long id;
    private String title;
    private String author;
}

