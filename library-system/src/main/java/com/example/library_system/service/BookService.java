package com.example.library_system.service;

import com.example.library_system.dto.BookCreationRequestDTO;
import com.example.library_system.dto.BookWithDetailsMappingDTO;
import com.example.library_system.dto.BookWithDetailsResponseDTO;
import com.example.library_system.entity.Author;
import com.example.library_system.entity.Book;
import com.example.library_system.exception_handling.BookNotFoundException;
import com.example.library_system.repository.AuthorRepository;
import com.example.library_system.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookWithDetailsMappingDTO dtoMapper;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository, BookWithDetailsMappingDTO dtoMapper) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.dtoMapper = dtoMapper;
    }

    //metod för paginering vid hämtning av alla böcker
    public Page<BookWithDetailsResponseDTO> getAllBooks(Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(pageable);
        return bookPage.map(dtoMapper::toDto); // Make sure dtoMapper.toDto(Book) returns BookWithDetailsResponseDTO
    }

    //metod för paginering vid sökning
    public Page<BookWithDetailsResponseDTO> searchBooks (String title, Long authorId, Pageable pageable) {
        return bookRepository
                .searchBooks(title, authorId, pageable)
                .map(dtoMapper::toDto);
    }

    //metod för custom JPQL-query för att söka böcker baserat på authorId. Returnerar flera resultat i form av en lista av DTOs med bokdetaljer.
    public List<BookWithDetailsResponseDTO> searchBooksByAuthorId (Long authorId) {
        return bookRepository.searchBooksByAuthorId(authorId)
                .stream()
                .map(dtoMapper::toDto)
                .collect(Collectors.toList());
    }

    //metod för custom JPQL-query för att söka bok baserat på titel. Returnerar ett enskilt resultat i form av en DTO med bokdetaljer. Optional används.
    public Optional<BookWithDetailsResponseDTO> searchUniqueBookByTitle (String title) {
        return bookRepository.searchUniqueBookByTitle(title)
                .map(dtoMapper::toDto);
    }

    //metod för native sql-query för att söka bok baserat på titel. Returnerar ett enskilt resultat i form av en DTO med bokdetaljer. Optional används.
    public Optional<BookWithDetailsResponseDTO> searchUniqueBookByTitleNativesql (String title) {
        return bookRepository.searchUniqueBookByTitleNativesql(title)
                .map(dtoMapper::toDto);
    }

    //metod för att hämta alla böcker
    public List<Book> getAllBooks () {

        return bookRepository.findAll();
    }

    //Visar här exempel på användning av custom exception (BookNotFoundException).
    // En ny instans av denna kastas fram om boken med inmatat ID inte kan hittas i databasen.
    //metod för att hämta bok baserat på id
    public BookWithDetailsResponseDTO getBookById (Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("The book with id: " + id + " could not be found"));
        return dtoMapper.toDto(book);

    }

    //metod för att söka bok på författare
    public List<BookWithDetailsResponseDTO> getBooksByAuthorName(String authorName) { //BookWithDetailsResponseDTO returneras
        List<Book> books = bookRepository.findByAuthor_FirstNameContainingOrAuthor_LastNameContaining(authorName, authorName);
        return books
                .stream()//För att mappa med dtoMapper
                .map(dtoMapper::toDto)
                .collect(Collectors
                .toList());
    }

   //metod för att söka bok på titel
    public List<BookWithDetailsResponseDTO> getBooksByTitle (String title) { //BookWithDetailsResponseDTO returneras
        List<Book> books = bookRepository.findByTitleContaining(title);
        return books.stream()//För att mappa med dtoMapper
                .map(dtoMapper::toDto)
                .collect(Collectors
                .toList());
    }

    //metod för att skapa ny bok
    //Exempel på att hantera IllegalArgumentException centralt i GlobalExceptionHandler
    public BookWithDetailsResponseDTO saveBook(BookCreationRequestDTO dto) {
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Author could not be found."));

        //Nedan valideras antal kopior och publikationsår när ny bok skapas
        if (dto.getAvailableCopies() > dto.getTotalCopies()) {
            throw new IllegalArgumentException("Available copies of a book can not be greater than Total copies of a book.");
        }
        int currentYear = java.time.Year.now().getValue(); //Skapa variabel currentYear, tilldela den värdet av nuvarande år.

        if (currentYear < dto.getPublicationYear()) {
            throw new IllegalArgumentException("Publication year can not be in the future");
        }
        Book book = dtoMapper.toEntity(dto, author);
        return dtoMapper.toDto(bookRepository.save(book));
    }
}

