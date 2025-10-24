// src/main/java/com/example/librarysystem/controller/TestController.java
//OBS! Denna klass testar kopplingen till databasen och antalet böcker i databasen.
package com.example.library_system.controller;

import com.example.library_system.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/test") //TESTA KOPPLING
    public String test() {
        return "Spring Boot is running!";
    }

    @GetMapping("/test/database") //TESTA ANTAL BÖCKER I DATABASEN
    public Map<String, Object> testDatabase() {
        Map<String, Object> result = new HashMap<>();
        result.put("message", "Database connection successful!");
        result.put("book_count", bookRepository.count());
        return result;
    }
}
