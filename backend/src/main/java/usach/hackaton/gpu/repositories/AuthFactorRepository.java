package usach.hackaton.gpu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.hackaton.gpu.entities.AuthFactor;

import java.util.List;

@Repository
public interface AuthFactorRepository extends JpaRepository<AuthFactor, Long> {
    List<AuthFactor> findByUserId(String userId);
}
