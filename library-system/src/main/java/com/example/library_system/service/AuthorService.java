package com.example.library_system.service;

import com.example.library_system.dto.AuthorDTO;
import com.example.library_system.dto.AuthorMappingDTO;
import com.example.library_system.dto.AuthorRequestDTO;
import com.example.library_system.entity.Author;
import com.example.library_system.exception_handling.AuthorNotFoundException;
import com.example.library_system.repository.AuthorRepository;
import com.example.library_system.validation.ValidationUtils;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMappingDTO authorMappingDTO;

    public AuthorService(AuthorRepository authorRepository, AuthorMappingDTO authorMappingDTO) {
        this.authorRepository = authorRepository;
        this.authorMappingDTO = authorMappingDTO;
    }

    //metod för att hämta alla författare
    public List<Author> getAllAuthors () {
        List<Author> authors = authorRepository.findAll();
        return authors.isEmpty() ? Collections.emptyList() : authors; //Detta är en villkorsats med strukturen: "VILLKOR" ? "VÄRDEOMSANT" : "VÄRDEOMFALSKT". Bra för att undvika nullpointerexception.
    }

    //metod för att hämta författare baserat på efternamn
    public List<AuthorDTO> getAuthorByLastName (String lastName) { //Lista pga olika författare skulle kunna ha samma efternamn dvs fler författare kan då returneras. Optional ej nödvändigt här pga det är en lista, en lista kan vara tom.
        List<Author> authors = authorRepository.findByLastNameContaining(lastName);

        if(authors.isEmpty()) {
                throw new AuthorNotFoundException("No authors could be found with last name: " + lastName);
            }
        return authors
                .stream()
                .map(authorMappingDTO::toDto)
                .collect(Collectors.toList());
    }

    //metod för att hämta författare baserat på id
    public Optional<Author> getAuthorById (Long id)
    {
        return authorRepository.findById(id);
    }

    //metod för att skapa ny författare
    public Author saveAuthor (AuthorRequestDTO dto) {
        Author author = authorMappingDTO.toEntity(dto);

        if (!ValidationUtils.validationOfAuthorCreation(dto)){ //Egentligen nog redundant eftersom @Valid används i Controllern.
            throw new IllegalArgumentException("Please make sure that you input into all fields!");
        }
        return authorRepository.save(author);
    }
}
