package com.example.library_system.dto;

public class BookWithDetailsResponseDTO {

    private Long id;
    private String title;
    private int availableCopies;
    private int publicationYear;
    private AuthorDTO author;

    public BookWithDetailsResponseDTO(Long id, String title, int availableCopies, int publicationYear, AuthorDTO author) {
        this.id = id;
        this.title = title;
        this.availableCopies = availableCopies;
        this.publicationYear = publicationYear;
        this.author = author;
    }

    public BookWithDetailsResponseDTO() {
    }

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

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }
}





