package com.example.library_system.controller;

import com.example.library_system.dto.RegularPersonUserRequestDTO;
import com.example.library_system.dto.UserMappingDTO;
import com.example.library_system.dto.UserRequestDTO;
import com.example.library_system.dto.UserResponseDTO;
import com.example.library_system.entity.User;
import com.example.library_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMappingDTO userMappingDTO;

    public UserController(UserService userService, UserMappingDTO userMappingDTO) {
        this.userService = userService;
        this.userMappingDTO = userMappingDTO;
    }

    //Endpoints nedan bygger ut sökväg från grund-url
    @GetMapping("/all") //Hämta alla användare
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            if (users.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(users);
            }
        } catch (Exception error) {
            System.err.println("Error when fetching users: " + error.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/email/{email}") //Hämta användare via email
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User could not be found"));
            return ResponseEntity.ok(userMappingDTO.toDto(user));

        } catch (Exception error) {
            System.err.println("Error when fetching user: " + error.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/new") //Admin skapar ny användare, denna endpoint är skyddad i SecurityConfig
    public ResponseEntity<UserResponseDTO> createNewUser(@Valid @RequestBody UserRequestDTO requestToUser) { //Valideringsfel från @Valid fångas och hanteras med GlobalExceptionHandler

            UserResponseDTO response = userService.createUser(requestToUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register") //Vanlig person registrerar sig som ny användare, denna endpoint är öppen i SecurityConfig
    public ResponseEntity<UserResponseDTO> registerNewUser (@Valid @RequestBody RegularPersonUserRequestDTO requestToCreateNewUser) { //Valideringsfel från @Valid fångas och hanteras med GlobalExceptionHandler

        UserResponseDTO response = userService.regularPersonRegistersUser(requestToCreateNewUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

    @GetMapping("/profile") //Visar olika profilinnehåll beroende på vilken roll som loggar in. Det finns endast två roller: Admin och User.
    public String userProfile (Authentication auth) {

        if(auth == null) {
            return "You are not logged in";
        }

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "Welcome Admin: " + auth.getName(); //Detta kommer att visa email, inte namn. Detta pga att i UserServiceImplementation är .username(user.getEmail())

        } else {
            return "Welcome User: " + auth.getName(); //Se ovan
        }
    }
}

