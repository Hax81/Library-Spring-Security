package com.example.library_system.dto;

import com.example.library_system.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMappingDTO {

    //Konvertera author-entitet till AuthorDTO
    public AuthorDTO toDto(Author author) {

        if (author == null) {
            return null;
        }

        AuthorDTO dto = new AuthorDTO();
        dto.setId(author.getId());
        dto.setFirstName(author.getFirstName());
        dto.setLastName(author.getLastName());
        return dto;
    }

    //Konvertera AuthorRequestDTO till Author-entitet. Vill kontrollera input.
    public Author toEntity(AuthorRequestDTO dto) {

        if(dto == null) {
            return null;
        }

        Author author = new Author();
        author.setFirstName(dto.getFirstName());
        author.setLastName(dto.getLastName());
        author.setBirthYear(dto.getBirthYear());
        author.setNationality(dto.getNationality());
        return author;
    }
}











