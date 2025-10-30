package usach.hackaton.gpu.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.hackaton.gpu.entities.AuthFactorTypeLookup;

@Repository
public interface AuthFactorTypeLookupRepository extends JpaRepository<AuthFactorTypeLookup, Integer> {
    Optional<AuthFactorTypeLookup> findByCode(String code);
}
