package pl.kurs.librarybooks.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateBookCommand {
    @NotNull
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String author;

    private Long studentId;
    @NotNull
    private boolean isBorrowed;

    private LocalDate borrowDate;
}
