package pl.kurs.librarybooks.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidEntityException extends RuntimeException{
    public InvalidEntityException(String message) {
        super(message);
    }
}
