package pl.kurs.librarybooks.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controller")
public class ClassController {

    @PostMapping
    public ResponseEntity<String> controller() {
        return ResponseEntity.ok("okej");
    }
}
