package com.example.library_system;

import com.example.library_system.dto.LoanRequestDTO;
import com.example.library_system.dto.LoanMappingDTO;
import com.example.library_system.entity.Book;
import com.example.library_system.entity.User;
import com.example.library_system.repository.AuthorRepository;
import com.example.library_system.repository.BookRepository;
import com.example.library_system.repository.LoanRepository;
import com.example.library_system.repository.UserRepository;
import com.example.library_system.service.LoanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) //JUnit vet att den ska använda Mockito så att man kan använda @Mock och @InjectMocks
public class UnittestLoanServiceAvailableCopies {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMappingDTO dtoMapper;

    @InjectMocks
    private LoanService loanService;

    @Test
    void ifBookHasZeroCopiesThenLoanShouldNotBePossible () {

        //ARRANGE
        User user = new User(); //Skapa testuser
        user.setId(1L);

        Book book = new Book(); //Skapa testbook
        book.setId(1L);
        book.setAvailableCopies(0); //AvailableCopies ska sättas till 0 i testet

        LoanRequestDTO loanRequestTestDTO = new LoanRequestDTO(1L, 1L); //DTO som har userID och bookId

        when(userRepository.findById(1L)).thenReturn(Optional.of(user)); //Simulering av att user finns
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book)); //Simulering av att book finns

        //ACT & ASSERT
        assertThrows(IllegalStateException.class, () -> {
            loanService.saveLoan(loanRequestTestDTO); //Anropa med mockade dependencies (annoterade ovan med @Mock)
        });
    }
}
