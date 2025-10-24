package com.example.library_system.controller;

import com.example.library_system.dto.BookCreationRequestDTO;
import com.example.library_system.dto.BookWithDetailsResponseDTO;
import com.example.library_system.service.AuthorService;
import com.example.library_system.service.BookService;
import com.example.library_system.validation.ValidationUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books") //Grund-url
public class BookController {

    private final BookService bookService;
    private final AuthorService authorService;

    public BookController(BookService bookService, AuthorService authorService) { //Konstruktorinjektion av BookService och AuthorService
        this.bookService = bookService;
        this.authorService = authorService;
    }

    //Endpoints nedan bygger ut sökväg från grund-url
    // Hämta alla böcker med paginering
    @GetMapping("/all")
    public ResponseEntity<Page<BookWithDetailsResponseDTO>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        try {
            ValidationUtils.validationOfPaginationPageAndSize(page, size); //Validera page och size genom att anropa valideringsmetoden i ValidationUtils
            Sort sortObj = Sort.by(Sort.Direction.fromString(direction), sort);
            Pageable pageable = PageRequest.of(page, size, sortObj);

            Page<BookWithDetailsResponseDTO> books = bookService.getAllBooks(pageable);

            if (books.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(books);

        } catch (Exception error) {
            System.err.println("Error fetching paged books: " + error.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Söka böcker med paginering
    @GetMapping("/search")
    public ResponseEntity<Page<BookWithDetailsResponseDTO>> searchBooks(@RequestParam(required = false) String title,
                                                                        @RequestParam(required = false) Long authorId,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(defaultValue = "title") String sort,
                                                                        @RequestParam(defaultValue = "asc") String direction) {
        try {
            ValidationUtils.validationOfPaginationPageAndSize(page, size); //Validera page och size genom att anropa valideringsmetoden i ValidationUtils
            Sort sortObj = Sort.by(Sort.Direction.fromString(direction), sort);
            Pageable pageable = PageRequest.of(page, size, sortObj);

            Page<BookWithDetailsResponseDTO> books = bookService.searchBooks(title, authorId, pageable);

            if (books.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(books);

        } catch (Exception error) {
            System.err.println("An error occurred when searching for books: " + error.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Custom Jpql: Söka böcker på authorId med custom Jpql
    @GetMapping("/customsearch")
    public ResponseEntity<List<BookWithDetailsResponseDTO>> searchBooksByAuthorId(@RequestParam(required = false)
                                                                                      Long authorId) {
        try {
            if (authorId != null) {
                List<BookWithDetailsResponseDTO> result = bookService.searchBooksByAuthorId(authorId);

                if (result.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(result);

                } else {
                    return ResponseEntity.badRequest().build();
                }

        } catch (Exception error) {
            System.err.println("An error occurred when searching for books: " + error.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
}

    //Custom Jpql: Söka bok på titel med custom Jpql
    @GetMapping("/customlookup")
    public ResponseEntity<BookWithDetailsResponseDTO> searchUniqueBookByTitle (@RequestParam(required = false)
                                                                                             String title) {
        try {
            if(title !=null) {
                Optional<BookWithDetailsResponseDTO> result = bookService.searchUniqueBookByTitle(title);

                if(result.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(result.get()); // Optional packas upp (genom att använda .get() ) innan svar returneras.
                                                        // Optional används i BookService men i API-svaret används den faktiska DTO:n
                                                        //Om Optional returnerats hade det funnits risk att JSON ser konstigt ut
                } else {
                return ResponseEntity.badRequest().build();
                }

        } catch (Exception error) {
            System.err.println("An error occurred when searching for the book: " + error.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Native sql: Söka bok på titel med native sql
    @GetMapping("/custom-native-lookup")
    public ResponseEntity<BookWithDetailsResponseDTO> searchUniqueBookByTitleNativeSql (@RequestParam(required = false) String title) {
        try {
            if(title !=null) {
                Optional<BookWithDetailsResponseDTO> result = bookService.searchUniqueBookByTitleNativesql(title);

                if(result.isEmpty()) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok(result.get()); //Se kommentar ovan

            } else {
                return ResponseEntity.badRequest().build();
            }

        } catch (Exception error) {
            System.err.println("An error occurred when searching for the book: " + error.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //Exempel på användning av custom exception istället för try/catch.
    //Anropar service-metoden "getBookById" som kastar fram BookNotFoundException om boken inte hittas.
    //Hämta bok baserat på id
    @GetMapping("/{id}")
    public ResponseEntity<BookWithDetailsResponseDTO> getBookById(@PathVariable Long id) {
        BookWithDetailsResponseDTO response = bookService.getBookById(id);
            return ResponseEntity.ok(response);
    }

    //Skapa ny bok
    //Exempel på Exception-hantering av IllegalArgumentException. Det sker här centralt i GlobalExceptionHandler. Även valideringsfel från @Valid fångas och hanteras centralt i GlobalExceptionHandler.
    @PostMapping("/new")
    public ResponseEntity<?> createBook(@Valid @RequestBody BookCreationRequestDTO dto) {
            BookWithDetailsResponseDTO response = bookService.saveBook(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}




