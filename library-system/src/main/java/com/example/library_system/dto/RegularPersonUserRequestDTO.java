package com.example.library_system.dto;

//Denna DTO är till för när vanlig person registrerar en user. Denna DTO saknar "role"

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegularPersonUserRequestDTO {
    @NotBlank(message = "Entering a first name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Entering a last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Entering an email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Entering a password is required")
    @Size(min = 8, message = "The password must be at least 8 characters")
    private String passWord; //Jag har även lösenordsvalidering (stor bokstav+siffra) i validationUtils-klassen.

    public RegularPersonUserRequestDTO(String firstName, String lastName, String email, String passWord) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passWord = passWord;
    }

    public RegularPersonUserRequestDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
