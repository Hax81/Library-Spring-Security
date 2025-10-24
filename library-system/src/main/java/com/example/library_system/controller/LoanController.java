package com.example.library_system.controller;

import com.example.library_system.dto.LoanRequestDTO;
import com.example.library_system.dto.LoanResponseDTO;
import com.example.library_system.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/all") //Visa alla lån
    public ResponseEntity<List<LoanResponseDTO>> getAllLoans() {
        try {
            List<LoanResponseDTO> loans = loanService.getAllLoans();
            if (loans.isEmpty()) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(loans);
            }

        } catch (Exception error) {
            System.err.println("Error when fetching loans: " + error.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/users/{userId}/loans") //Hämta lån baserat på användar-Id
    public ResponseEntity<List<LoanResponseDTO>> getLoansByUserId(@PathVariable Long userId) {
        try {
            List<LoanResponseDTO> loans = loanService.getLoansByUserId(userId);
            if (loans.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(loans);
        } catch (Exception error) {
            System.err.println("Error when fetching loans: " + error.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/new") //Nytt lån
    public ResponseEntity<LoanResponseDTO> createNewLoan(@Valid @RequestBody LoanRequestDTO requestToUser) { //Exceptions hanteras i GlobalExceptionHandler

            LoanResponseDTO responseToUser = loanService.saveLoan(requestToUser);//anropa metoden saveLoan i loanService
            return ResponseEntity.status(HttpStatus.CREATED).body(responseToUser);

    }

    @PutMapping("/{loanId}/return") //Återlämna. OBS att det är LÅNETS Id som anges när metoden testas.
    public ResponseEntity<LoanResponseDTO> returnLoan(@PathVariable Long loanId) {
        try {
            LoanResponseDTO responseToUser = loanService.returnBook(loanId); //anropa metoden returnBook i loanService
            return ResponseEntity.ok(responseToUser);
        } catch (Exception error) {
            System.err.println("Error when returning loan: " + error.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{loanId}/extend") //Förläng lån
    public ResponseEntity<LoanResponseDTO> extendLoan(@PathVariable Long loanId) {
        try {
            LoanResponseDTO responseToUser = loanService.extendLoan(loanId);
            return ResponseEntity.ok(responseToUser);

        } catch (Exception error) {
            System.err.println("Error when extending loan: " + error.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

