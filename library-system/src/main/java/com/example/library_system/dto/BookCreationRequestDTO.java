package com.example.library_system.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class BookCreationRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 50, message = "Title must at least 1 and at most 50 characters")
    private String title;

    @Min(value = 1000, message = "Publication year must be after year 1000")
    private int publicationYear;

    @Min(value = 0, message = "The number of available copies must be at least 0")
    private int availableCopies;

    @Min(value = 1, message = "The number of total copies must be at least 1" )
    private int totalCopies;

    @NotNull(message = "AuthorId is required")
    private Long authorId;

    public BookCreationRequestDTO() {
    }

    public BookCreationRequestDTO(String title, int publicationYear, int availableCopies, int totalCopies, Long authorId) {
        this.title = title;
        this.publicationYear = publicationYear;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
        this.authorId = authorId;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getPublicationYear() { return publicationYear; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) { this.totalCopies = totalCopies; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
}







