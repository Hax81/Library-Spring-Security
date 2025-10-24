package com.example.library_system.entity;

//Att skapa LOAN är att lägga in BookId och UserId.

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans") //Måste matcha tabellnamnet i databasen
public class Loan {
    @Id //Primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Autoinkrement
    @Column(name = "loan_id") // alla måste matcha kolumnnamnet i DB
    private Long loanId;

    //Relation
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) //DETTA ÄR FOREIGN KEY I LOANS-TABELLEN
    private User user; //Refererar hela user-objektet istället för user_id.

    //Relation
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "book_id", nullable = false) //DETTA ÄR FOREIGN KEY I LOANS-TABELLEN
    private Book book; //Refererar hela book-objektet istället för book_id

    @Column(name = "borrowed_date") //"Date" Ska eg vara "LocalDateTime. Ändra.
    private LocalDateTime borrowedDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate; //Ska läggas manuellt

    @Column(name = "returned_date")
    private LocalDateTime returnedDate; //Ska vara "null" från början

    public Loan(User user, Book book, LocalDateTime borrowedDate, LocalDateTime dueDate, LocalDateTime returnedDate) {
        this.user = user;
        this.book = book;
        this.borrowedDate = borrowedDate;
        this.dueDate = dueDate;
        this.returnedDate = returnedDate;
    }

    public Loan() {
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDateTime getBorrowedDate() {
        return borrowedDate;
    }

    public void setBorrowedDate(LocalDateTime borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnedDate() {
        return returnedDate;
    }

    public void setReturnedDate(
            LocalDateTime returnedDate) {
        this.returnedDate = returnedDate;
    }
}
