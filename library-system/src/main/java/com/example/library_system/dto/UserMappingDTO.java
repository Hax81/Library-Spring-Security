package com.example.library_system.dto;

import com.example.library_system.entity.User;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class UserMappingDTO {

    //Konvertera User-entitet till UserResponseDTO för att kontrollera vad som visas (vill ej visa password)
    public UserResponseDTO toDto (User user) {

        if (user == null) { //If-check för att hantera om user är null.
            return null;
        }

        UserResponseDTO dto = new UserResponseDTO(); //Skapar objekt dto av klassen UserResponseDTO. Detta är inte en bean och injiceras därför inte. Det är ett vanligt java-objekt.
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRegistrationDate(user.getRegistrationDate());

        return dto;
    }

    //Konvertera UserRequestDTO till Entitet (för att spara den). Vill kontrollera input.
    public User toEntity (UserRequestDTO dto) {

        if (dto == null) { //if-check för att hantera om dto är null.
            return null;
        }

        User user = new User(); //Skapar nytt objekt user av User-klassen
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassWord(dto.getPassWord());
        user.setRegistrationDate(LocalDateTime.now());

        return user;
    }
}
