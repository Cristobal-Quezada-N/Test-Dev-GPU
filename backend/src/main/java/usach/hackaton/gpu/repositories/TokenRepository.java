package usach.hackaton.gpu.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import usach.hackaton.gpu.entities.ActivationToken;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<ActivationToken, Long> {
    Optional<ActivationToken> findByEmailToken(String token);
}
