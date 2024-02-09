package pl.kurs.librarybooks.exceptionhandling;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.kurs.librarybooks.exceptions.InvalidEntityException;
import pl.kurs.librarybooks.exceptions.InvalidIdException;

import java.nio.file.NoSuchFileException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleEntityNotFoundException(EntityNotFoundException e) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(
                List.of(e.getMessage()),
                "NOT_FOUND",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler({InvalidEntityException.class, InvalidIdException.class})
    public ResponseEntity<ExceptionResponseDTO> handleEntityException(RuntimeException e) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(
                List.of(e.getMessage()),
                "BAD_REQUEST",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionResponseDTO> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        ExceptionResponseDTO response = new ExceptionResponseDTO(
                List.of(e.getMessage()),
                "BAD_REQUEST",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<ExceptionResponseDTO> handleNoSuchFileException(NoSuchFileException e) {

        ExceptionResponseDTO response = new ExceptionResponseDTO(
                List.of("Not found: " +e.getFile()),
                "BAD_REQUEST",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errorMessages = e.getFieldErrors().stream()
                .map(fe -> "field: " + fe.getField() + " / rejected value: " + fe.getRejectedValue() + " / message: " +fe.getDefaultMessage())
                .collect(Collectors.toList());
        ExceptionResponseDTO response = new ExceptionResponseDTO(
                errorMessages,
                "BAD_REQUEST",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
