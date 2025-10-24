package com.example.library_system.service;

import com.example.library_system.dto.LoanMappingDTO;
import com.example.library_system.dto.LoanRequestDTO;
import com.example.library_system.dto.LoanResponseDTO;
import com.example.library_system.entity.Loan;
import com.example.library_system.entity.User;
import com.example.library_system.entity.Book;
import com.example.library_system.repository.BookRepository;
import com.example.library_system.repository.LoanRepository;
import com.example.library_system.repository.UserRepository;
import com.example.library_system.validation.ValidationUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository; //Måste ha för att kunna hämta User
    private final BookRepository bookRepository; //Måste ha för att kunna hämta Book
    private final LoanMappingDTO dtoMapper; //Måste ha för att kunna använda LoanMappingDTO (dtoMapper) här i service

    public LoanService(LoanRepository loanRepository, UserRepository userRepository, BookRepository bookRepository, LoanMappingDTO dtoMapper) { //Konstruktorinjektion av dessa

        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.dtoMapper = dtoMapper;
    }

    //metod för att hämta alla lån
    public List<LoanResponseDTO> getAllLoans () {
        List<Loan> loans = loanRepository.findAll();
        return loans.isEmpty() ? Collections.emptyList() : loans.stream().map(dtoMapper::toDto).toList(); //Om listan med loans är tom så returnera tom Collections-lista, om listan ej är tom så mappa varje lån-entitet med dtoMapper till en LoanResponseDTO.
    }//"Ternary operator"

    //metod för att hämta användares lån baserat på Id
    public List<LoanResponseDTO> getLoansByUserId (Long userId) { //LoanResponseDTO returneras. DTO för att annars returneras lösenord också. Jsonignore kunde iofs bara använts.
        List<Loan> loans = loanRepository.findLoansByUserId(userId);
        return loans //för att mappa med dtoMapper
                .stream()
                .map(dtoMapper::toDto)
                .toList();
    }

    //metod för att låna bok
    @Transactional(timeout=30)
    public LoanResponseDTO saveLoan (LoanRequestDTO loanRequestDTO) {
        User user = userRepository.findById(loanRequestDTO.getUserId()).orElseThrow(() -> new IllegalArgumentException("User not found")); //Hitta User. Hantera om user ej hittas.
        Book book = bookRepository.findById(loanRequestDTO.getBookId()).orElseThrow(() -> new IllegalArgumentException("Book not found")); //Hitta Book. Hantera om bok ej hittas. (JPA:s inbyggda findById används -> Optional returneras -> .orElse möjliggörs.

        //Kontrollera om boken finns innan den lånas ut genom att anropa valideringsmetoden i ValidationUtils.
        ValidationUtils.validationOfBookLoan(book);

        //Går vidare hit om boken finns, dvs kan lånas ut. Uppdatera antal böcker genom att hämta antal kopior och ta bort 1 st (available copies) om utlån godkänns. Vi bestämmer här att endast en kopia kan lånas ut åt gången.
        book.setAvailableCopies(book.getAvailableCopies() -1);
        bookRepository.save(book);//MÅSTE HÄR VERKLIGEN VARA EN BOOKSAVE? JPA persistar väl bokens uppdaterade AvailableCopies automatiskt när lån exekveras iom den (boken) hittas med findById vilket är inbyggt i JPA.

        //Skapar loan-objekt i LoanMappingDTO. Använder LoanMappingDTO (dtoMapper).
        Loan loan = dtoMapper.toEntity(loanRequestDTO, user, book);

        loan.setBorrowedDate(LocalDateTime.now()); //Sätter BorrowedDate till aktuell tid då lånet läggs.

        loan.setDueDate(LocalDateTime.now().plusDays(14)); //Sätter dueDate till 14 dagar från lånets aktuella tid.

        loan.setReturnedDate(null); //Sätter Returneddate till null.

        //Sparar lån
        loanRepository.save(loan);

        //Returnera den mappade DTO:n
        return dtoMapper.toDto(loan);
    }

    //metod för att återlämna bok (loans/{id}/return) dvs utgå från LÅNETS Id, kom ihåg att läsa instruktionerna ordentligt
    public LoanResponseDTO returnBook (Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("Loan with id " + " " + loanId +" " + "can not be found")); //Optional ger tillgång till .orElseThrow(). OBS att detta först verkade konstigt pga .orElse() ej används med List - Här används dock .orElse() med den inbyggda metoden findById som tillhandahålls av JPA.
                                                                                                                                                    //Throw är nyckelordet för att "slänga fram en exception". new RuntimeException är själva exception-objektet som "slängs fram".
            if(loan.getReturnedDate() != null) { //Om returnedDate INTE är null betyder det att returnedDate har ett värde, det vill säga boken ÄR återlämnad redan.
               throw new RuntimeException("This book has already been returned"); //glömde först "throw". Detta måste vara med för, annars händer inget.
            }
        loan.setReturnedDate(LocalDateTime.now()); //Sätt återlämnningsdatum och tid
        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() +1); //sätter antal kopior genom att hämta antal kopior och
                                        // lägga till 1 st. Vi bestämmer här att endast 1 st kan återlämnas åt gången.
        loanRepository.save(loan);      //se till att uppdatering sker (som vid lån ovan)
        return dtoMapper.toDto(loan);
    }

    //metod för att förlänga lånetiden
    public LoanResponseDTO extendLoan (Long loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new RuntimeException("The loan with id " + " " + loanId + " " + "can not be found")); //Se metod ovan

        if(loan.getReturnedDate() != null) { //Se metod ovan
            throw new RuntimeException("This book is not currently loaned out. Therefore, there is no loan to extend");
        }

    loan.setDueDate(loan.getDueDate().plusDays(14)); //Utökar dagarna på lånet med 14 st.
                        // Väljer 14 pga det är standard vid utlåning. Verkar logiskt att lägga till samma antal dagar.
    loanRepository.save(loan); //se metod ovan

    return dtoMapper.toDto(loan); //dtoMapper (dvs LoanMappingDTO) konverterar Loan-entiteten till en instans
                                // (ett objekt) av LoanResponseDTO-klassen och returnerar till klienten.
    }
}



