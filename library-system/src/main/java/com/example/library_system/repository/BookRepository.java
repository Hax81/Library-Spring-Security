// src/main/java/com/example/librarysystem/repository/BookRepository.java
package com.example.library_system.repository;

import com.example.library_system.entity.Author;
import com.example.library_system.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    //Paginering: Söka en bok genom titel med paginering
    Page<Book> findByTitleContainingIgnoreCase (String title, Pageable pageable);

    //Paginering: Söka med flera parametrar
    @Query("SELECT b FROM Book b WHERE " +
            "(:title IS NULL OR LOWER(b.title) LIKE LOWER (CONCAT('%', :title, '%'))) AND " +
            "(:authorId IS NULL OR b.author.id = :authorId)")
    Page<Book> searchBooks(@Param("title") String title,
                           @Param("authorId") Long authorId,
                           Pageable pageable);

    //JPQL: Söka en bok med authorId. Möjliggöra att returnera flera resultat genom att använda List.
    @Query("SELECT b FROM Book b WHERE b.author.id = :authorId")
    List<Book> searchBooksByAuthorId(@Param("authorId")Long authorId);

    //JPQL: Söka en bok med titel. Returnera ett enda resultat genom att använda Optional.
    @Query("SELECT b FROM Book b WHERE b.title =:title")
    Optional<Book> searchUniqueBookByTitle(@Param("title")String title);

    //Native SQL: Söka en bok på titel och authorId med native Sql-query. Returnera ett enda resultat genom att använda Optional.
    @Query(value = "SELECT * FROM book WHERE title = :title", nativeQuery = true)
    Optional<Book> searchUniqueBookByTitleNativesql(@Param("title")String title);

    List<Book> findByAuthor_FirstNameContainingOrAuthor_LastNameContaining(String firstName, String lastName);

    List<Book> findByTitleContaining (String word);
}


