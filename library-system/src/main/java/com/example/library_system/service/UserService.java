package com.example.library_system.service;

import com.example.library_system.dto.RegularPersonUserRequestDTO;
import com.example.library_system.dto.UserMappingDTO;
import com.example.library_system.dto.UserRequestDTO;
import com.example.library_system.dto.UserResponseDTO;
import com.example.library_system.entity.User;
import com.example.library_system.repository.UserRepository;
import com.example.library_system.validation.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMappingDTO userMapper;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, UserMappingDTO userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder =passwordEncoder;
    }

    //metod för att hämta alla användare
    public List<User> getAllUsers () {
       List<User> users = userRepository.findAll();
       return users.isEmpty() ? Collections.emptyList() : users; //Detta är en villkorsats med strukturen: "VILLKOR" ? "VÄRDEOMSANT" : "VÄRDEOMFALSKT".
    }

    //metod för att hämta användare baserat på email
    public Optional<User> getUserByEmail (String email) {

        if (! ValidationUtils.validationOfEmail(email)) { //Anropa metoden för validering av email
            throw new IllegalArgumentException("Invalid email format!");
        }
        return userRepository.findByEmail(email);
    }

    //metod för att Admin skapar ny användare. Kan skapa en vanlig User ELLER en Admin.
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {

        logger.info("event=user_creation email={} role={}", userRequestDTO.getEmail(), userRequestDTO.getRole()); //Strukturerad loggning av säkerhetshändelse

        if (!ValidationUtils.validationOfUserCreation(userRequestDTO)) { //Anropa metoden för validering av data finns i alla inputfält
            throw new IllegalArgumentException("Please input all fields!");
        }

        ValidationUtils.validationOfPassword(userRequestDTO.getPassWord()); //Validera lösenord

        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Error! User already exists");
        }

        User user = userMapper.toEntity(userRequestDTO); //använd UserMappingDTO (userMapper)
        user.setPassWord(passwordEncoder.encode(userRequestDTO.getPassWord())); //Kryptera lösenord
        String role = userRequestDTO.getRole().toUpperCase(); //Använd role från userRequestDTO och validera att det är antingen "USER" eller "ADMIN".
        if (!role.equals("USER") && !role.equals("ADMIN")) {
            throw new IllegalArgumentException("Assigned role is invalid! Role must only be either USER or ADMIN");
        }
        user.setRole(role);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser); // returnera DTO
    }

    //metod för att vanlig person registrerar sig som ny användare. Blir alltid automatiskt vanlig User.
    public UserResponseDTO regularPersonRegistersUser (RegularPersonUserRequestDTO requestToCreateNewUser) {

        logger.info("event=user_creation email={} role={}", requestToCreateNewUser.getEmail(), "USER"); //Strukturerad loggning av säkerhetshändelse

        if (userRepository.findByEmail(requestToCreateNewUser.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Error! User already exists");
        }
        ValidationUtils.validationOfPassword(requestToCreateNewUser.getPassWord()); //Validera lösenord

        User user = new User();
        user.setFirstName(requestToCreateNewUser.getFirstName());
        user.setLastName(requestToCreateNewUser.getLastName());
        user.setEmail(requestToCreateNewUser.getEmail());
        user.setPassWord(passwordEncoder.encode(requestToCreateNewUser.getPassWord())); //Kryptera lösenord
        user.setRegistrationDate(LocalDateTime.now());
        user.setRole("USER");  //Hårdkodar så att alla konton som skapas av vanlig person ska bli User. Fältet "role" ignoreras alltid här. Borde EGENTLIGEN använda en annan DTO där fältet "role" inte finns.
        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}



