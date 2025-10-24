package com.example.library_system.dto;

import com.example.library_system.entity.Author;
import com.example.library_system.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookWithDetailsMappingDTO {

    // Konvertera Book-entitet till BookWithDetailsResponseDTO för att kontrollera vad som visas
    public BookWithDetailsResponseDTO toDto(Book book) {

        if (book == null || book.getAuthor() == null) { //If-check för att undvika nullpointerexception om book eller author är null.
            return null;
        }

        Author author = book.getAuthor();

        AuthorDTO authorDTO = new AuthorDTO(); //Skapa objekt "authorDTO" av AuthorDTO-klassen. Ej bean, injiceras därför inte.
        authorDTO.setId(author.getId());
        authorDTO.setFirstName(author.getFirstName());
        authorDTO.setLastName(author.getLastName());

        BookWithDetailsResponseDTO dto = new BookWithDetailsResponseDTO(); //Skapa objekt "dto" av BookWithDetailsResponseDTO-klassen. Ej bean, injiceras därför inte.
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAvailableCopies(book.getAvailableCopies());
        dto.setPublicationYear(book.getPublicationYear());
        dto.setAuthor(authorDTO); //objektet authorDTO används

        return dto;
    }

    // Konvertera RequestDTO till Book entitet (för att spara den). Vill kontrollera input.
    public Book toEntity(BookCreationRequestDTO dto, Author author) {
        if (dto == null || author == null) return null;

        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setPublicationYear(dto.getPublicationYear());
        book.setAvailableCopies(dto.getAvailableCopies());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAuthor(author);

        return book;
    }
}
