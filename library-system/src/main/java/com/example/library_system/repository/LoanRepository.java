package com.example.library_system.repository;

import com.example.library_system.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository <Loan, Long> {
    //Custom metod. Detta ska autogenerera en query som är: SELECT * FROM loans WHERE user_id = ?

    List <Loan> findLoansByUserId (Long userId);

    /*Detta fungerar eftersom Loan har ett user-fält som är ett objekt av
    klassen User (User user står i Loan), och Spring kan navigera till user.id.*/
}
