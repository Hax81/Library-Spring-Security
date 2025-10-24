package com.example.library_system;

import com.example.library_system.dto.LoanRequestDTO;
import com.example.library_system.dto.LoanResponseDTO;
import com.example.library_system.entity.Author;
import com.example.library_system.entity.Book;
import com.example.library_system.entity.User;
import com.example.library_system.repository.AuthorRepository;
import com.example.library_system.repository.BookRepository;
import com.example.library_system.repository.UserRepository;
import com.example.library_system.service.LoanService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.transaction.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootTest //från Spring. Gör så att Spring startar de saker som behövs i testmiljön för denna test-klass.
@Transactional //rollback efter att test är klart så db ej ändras
public class IntegrationTestDuedate {

    @Autowired //autoinjektion av instanserna
    private LoanService loanService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private UserRepository userRepository;

    @Test //JUnit-märker metoden som en test-metod.
    void dueDateShouldBeSetTo14DaysWhenCreatingLoan () {

        //ARRANGE
        User user = new User(); //Skapa user-objekt för testning
            user.setFirstName("TestpersonFirstName");
            user.setLastName("TestpersonLastName");
            user.setEmail("Test@Testmail.com");
            user.setPassWord("TestPassWord");
            user.setRegistrationDate(LocalDateTime.now());
            user=userRepository.save(user);

        Author author = new Author(); //Skapa author-objekt för testning
            author.setFirstName("TestAuthorFirstName");
            author.setLastName("TestAuthorLastName");
            author.setBirthYear(1990);
            author.setNationality("TestNation");
            author = authorRepository.save(author);

        Book book = new Book(); //Skapa book-objekt för testning
            book.setTitle("Test Book");
            book.setAuthor(author);
            book.setAvailableCopies(2);
            book.setTotalCopies(2);
            book = bookRepository.save(book);

            LoanRequestDTO loanRequestTestDTO = new LoanRequestDTO(user.getId(), book.getId());

        //ACT
            LoanResponseDTO response = loanService.saveLoan(loanRequestTestDTO);

        //ASSERT
            LocalDate theExpectedDueDate = LocalDate.now().plusDays(14);
            assertEquals(theExpectedDueDate, response.getDueDate().toLocalDate());
    }
}
