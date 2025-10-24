package com.example.library_system.repository;

import com.example.library_system.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    /*"Author" refererar till Author-klassen
     (entiteten) och "Long" refererar till typen av Primary Key i Author-entiteten.*/

    List<Author> findByLastNameContaining (String lastName);

    /*Lista pga olika författare skulle kunna ha samma
efternamn dvs fler författare kan då returneras. Optional ej nödvändigt här pga det är en lista, en lista kan vara tom.*/

}
