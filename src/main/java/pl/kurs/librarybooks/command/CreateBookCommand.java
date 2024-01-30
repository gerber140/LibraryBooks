package pl.kurs.librarybooks.command;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookCommand {
    @NotBlank
    private String title;
    @NotBlank
    private String author;
}
