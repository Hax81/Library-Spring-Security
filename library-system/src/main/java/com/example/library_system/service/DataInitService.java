package com.example.library_system.service;
//Klass för att skapa en Admin när appen startar

import com.example.library_system.entity.User;
import com.example.library_system.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class DataInitService {

    @Autowired
    UserRepository userRepository; //Injicerar userRepository för att kunna använda dess metoder här

    @Autowired
    PasswordEncoder passwordEncoder; //Injicerar passwordencoder för att kunna använda dess metoder här

    @PostConstruct
    public void initData () {

        String email ="Axelssontest@mail.com";

        userRepository.findByEmail(email).ifPresentOrElse(
            existingUser -> {
                System.out.println("Email already exists: " + existingUser.getEmail());
            }, () -> {

                User user = new User();
                user.setFirstName("Henrik");
                user.setLastName("Axelsson");
                user.setEmail("Axelssontest@mail.com");
                user.setPassWord(passwordEncoder.encode("1234567890"));
                user.setRegistrationDate(LocalDateTime.now());
                user.setRole("ADMIN");
                user.setEnabled(true);

                userRepository.save(user);
                System.out.println("Admin är skapad");
        }
        );
    }
}


