package pl.kurs.librarybooks.command;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@AllArgsConstructor
public class CreateBookCommand {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
}
