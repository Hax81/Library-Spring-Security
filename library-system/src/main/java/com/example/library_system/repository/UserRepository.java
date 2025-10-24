package com.example.library_system.repository;

import com.example.library_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail (String email); /*Optional ger tillgång till olika metoder, t.ex orElse() och map()
    som kan användas i UserService och sedan i UserController. Används ej vid listor. Måste deklareras här eftersom det
    är en custom-metod. T.ex findById finns inbyggt i JPA men findByEmail gör inte det. findByEmail returnerar
    alltid Optional vilket betyder att t.ex .orElse då kan användas.*/

    boolean existsByEmail (String email); //För att kontrollera om email redan finns. Spring Data nyckelord: "existsBy"
}
