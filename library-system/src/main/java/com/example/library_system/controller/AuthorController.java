package com.example.library_system.controller;

import com.example.library_system.dto.AuthorDTO;
import com.example.library_system.dto.AuthorMappingDTO;
import com.example.library_system.dto.AuthorRequestDTO;
import com.example.library_system.entity.Author;
import com.example.library_system.service.AuthorService;
import com.example.library_system.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final BookService bookService;
    private final AuthorMappingDTO authorMappingDTO;

    public AuthorController(AuthorService authorService, BookService bookService, AuthorMappingDTO authorMappingDTO) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.authorMappingDTO = authorMappingDTO;
    }

    //Endpoints nedan bygger ut sökväg från grund-url
    // Hämta alla författare
    @GetMapping("/all")
    public ResponseEntity<List<Author>> getAllAuthors() {
        try {
            List<Author> authors = authorService.getAllAuthors();
            if (authors.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(authors);
            }
        } catch (Exception error) {
            System.err.println("Error when fetching authors: " + error.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Hämta författare via efternamn
    @GetMapping("/name/{lastName}")
    public ResponseEntity<List<AuthorDTO>> getAuthorByLastName(@PathVariable String lastName) { //Returnerar AuthorDTO istället för Author-entiteten
        List<AuthorDTO> authorDTOS = authorService.getAuthorByLastName(lastName); //Lista med authors-objekt av klassen Author
        return ResponseEntity.ok(authorDTOS);
    }

    //Skapa ny författare
    @PostMapping("/new")
    public ResponseEntity<?> createAuthor(@Valid @RequestBody AuthorRequestDTO dto) {/*Eftersom det är POST så ska
                                                                                    @Requestbody användas. JSON-datan
                                                                                    ska parsas (tolkas och omvandlas) till ett java-objekt.
                                                                                    "Skapa ett java-objekt "author" av klassen "Author".
                                                                                    Exceptions hanteras Globalt av Global ExceptionHandler.*/
            Author savedAuthor = authorService.saveAuthor(dto);
            AuthorDTO response = authorMappingDTO.toDto(savedAuthor);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}


