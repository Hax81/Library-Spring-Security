package com.example.library_system.dto;

//DTO för input av Loan. Endast userId och bookId inhämtas.

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class LoanRequestDTO {

    @NotNull(message = "UserId is required")
    @Min(value = 1, message = "UserId must be a positive whole number")
    private Long userId;

    @NotNull(message = "BookId is required")
    @Min(value=1, message = "BookId must be a positive whole number")
    private Long bookId;

    public LoanRequestDTO(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    public LoanRequestDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
}

