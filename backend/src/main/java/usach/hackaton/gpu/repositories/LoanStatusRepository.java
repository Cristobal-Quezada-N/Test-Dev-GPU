package usach.hackaton.gpu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.hackaton.gpu.entities.LoanStatus;

@Repository
public interface LoanStatusRepository extends JpaRepository<LoanStatus, Long> {
}
