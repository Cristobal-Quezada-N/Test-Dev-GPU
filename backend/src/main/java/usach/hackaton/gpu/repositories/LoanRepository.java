package usach.hackaton.gpu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.hackaton.gpu.entities.Loan;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
}
