// src/main/java/com/example/librarysystem/entity/Book.java
package com.example.library_system.entity;

import com.fasterxml.jackson.annotation.JsonBackReference; //Läs dokumentation om jackson
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "books")  // Måste matcha tabellnamnet i databasen
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")  // alla måste matcha kolumnnamnet i DB
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "publication_year")
    private int publicationYear;

    @Column(name = "available_copies")
    private int availableCopies;

    @Column(name = "total_copies")
    private int totalCopies;

    //Relation med Author
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false) //DETTA ÄR FOREIGN KEY I BOOKS-TABELLEN i sqlite-databasen
    @JsonBackReference
    private Author author;

    //Relation med Loan
    @OneToMany(mappedBy = "book") //Ska EJ  vara i konstruktorn. Jag vill ej skapa en lista av lån varje gång jag skapar ett book-objekt. Dessutom undviks cirklära-referens-problemet.
    @JsonBackReference
    private List<Loan> loans; //En bok kan ha många loans över tid


    public Book(String title, int publicationYear, int availableCopies, int totalCopies, Author author) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.author = author;
    }

    // Default constructor krävs av JPA
    public Book() {}



    // Getters och setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

}