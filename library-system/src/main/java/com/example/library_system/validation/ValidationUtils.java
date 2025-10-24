package com.example.library_system.validation;

import com.example.library_system.dto.AuthorRequestDTO;
import com.example.library_system.dto.UserRequestDTO;
import com.example.library_system.entity.Book;

//Klass för att samla valideringsmetoder på ett ställe
public class ValidationUtils {

    //Metod för att validera skapande av ny Author
    public static boolean validationOfAuthorCreation (AuthorRequestDTO dto) {
        if (dto == null) {
            return false;
        }
        if(dto.getFirstName() == null || dto.getLastName() == null || dto.getBirthYear() <=0 || dto.getNationality() == null) {
            return false;
        } else {
            return true;
        }
    }

    //Metod för att validera email
    public static boolean validationOfEmail(String email) {
        if(email == null) { //if-check för att undvika nullpointerexception om email är tom
            return false;
        }
        String emailRegex = "^[\\w-\\.]+@[\\w-\\.]+\\.[a-z]{2,}$";
        return email.matches(emailRegex);
    }

    //Metod för att validera skapande av ny User
    public static boolean validationOfUserCreation(UserRequestDTO dto) {
        if (dto == null) {
            return false;
        }
        if(dto.getFirstName() ==null || dto.getLastName() ==null || dto.getEmail() ==null || dto.getPassWord() == null || dto.getRole() == null) {
            return false;
        } else {
            return true;
        }
    }

    //Metod för att validera att bok finns innan den lånas ut
    public static void validationOfBookLoan (Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Oh no! Book is not allowed to be null");
        }
        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("Oh no! This book is currently unavailable for loan. Please try another title");
        }
    }

    //Metod för att validera page och size i samband med paginering
    public static void validationOfPaginationPageAndSize(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("The page index is not allowed to be be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("The page size must be greater than zero");
        }
    }

    //Metod för att validera lösenord. Lösenordet måste vara minst 8 tecken med kombinerade bokstäver och siffror
    public static void validationOfPassword (String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("You must enter a password");
        }

        if(password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }

        if(!password.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("Password must contain at least one uppercase letter");
        }

        if(!password.matches(".*[0-9].*")) {
            throw new IllegalArgumentException("Password must contain at least one number");
        }

        if(!password.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("Password must contain at least one lowercase letter");
        }
    }
}
