package com.example.library_system.dto;

import com.example.library_system.entity.Loan;
import com.example.library_system.entity.Book;
import com.example.library_system.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LoanMappingDTO {

    //Konvertera Loan-entitet till LoanResponseDTO för att kontrollera vad som visas (vill t.ex ej ha med passWord från user-objektet och vill ej ha med onödig bokinfo)
    public LoanResponseDTO toDto (Loan loan) {

        if (loan == null) { //if check för att undvika nullpointerexception om loan är null.
            return null;
        }

        LoanResponseDTO dto = new LoanResponseDTO(); //Skapar objekt av klassen LoanResponseDTO. Är inte en bean, är ett vanligt Java-objekt.
        dto.setLoanId(loan.getLoanId());
        dto.setBookTitle(loan.getBook().getTitle());
        dto.setBookId(loan.getBook().getId());
        dto.setLoanerName(loan.getUser().getFirstName() + " " + loan.getUser().getLastName()); //Vill ha med användare i svaret då bok lånas för att se namn på den som lånat. Ev. bra för skalbarhet senare.
        dto.setBorrowedDate(loan.getBorrowedDate());
        dto.setDueDate(loan.getDueDate());
        dto.setReturnedDate(loan.getReturnedDate());

        return dto;
    }

    //Konvertera LoanRequestDTO till Entitet (för att spara den).

    public Loan toEntity(LoanRequestDTO dto, User user, Book book) {

        if (dto == null || user == null || book == null) { //If-check för att undvika nullpointerexception
            return null;
        }

        Loan loan = new Loan(); //Skapar Loan-objekt.
        loan.setUser(user);
        loan.setBook(book);

        return loan;
    }
}

