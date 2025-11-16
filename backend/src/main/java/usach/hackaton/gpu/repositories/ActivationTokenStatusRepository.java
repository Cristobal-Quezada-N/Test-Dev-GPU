package usach.hackaton.gpu.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import usach.hackaton.gpu.entities.ActivationTokenStatus;

public interface ActivationTokenStatusRepository extends JpaRepository<ActivationTokenStatus, Long> {
    Optional<ActivationTokenStatus> findByCode(String code);
}
