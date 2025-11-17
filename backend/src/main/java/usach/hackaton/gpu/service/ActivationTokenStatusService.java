package usach.hackaton.gpu.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.ActivationTokenStatus;
import usach.hackaton.gpu.enums.ActivationTokenStatusCode;
import usach.hackaton.gpu.repositories.ActivationTokenStatusRepository;

@Service
@RequiredArgsConstructor
public class ActivationTokenStatusService {
    private final ActivationTokenStatusRepository activationTokenStatusRepository;

    Optional<ActivationTokenStatus> findByCode(ActivationTokenStatusCode code) {
        return activationTokenStatusRepository.findByCode(code.name());
    }

    private ActivationTokenStatus getByCode(ActivationTokenStatusCode code) {
        return findByCode(code).orElseThrow(() -> new IllegalStateException(code + "status code not found"));
    }

    public ActivationTokenStatus getPending() {
        return getByCode(ActivationTokenStatusCode.PENDING);
    }

    public ActivationTokenStatus getExpired() {
        return getByCode(ActivationTokenStatusCode.EXPIRED);
    }

    public ActivationTokenStatus getRevoked() {
        return getByCode(ActivationTokenStatusCode.REVOKED);
    }

    public ActivationTokenStatus getUsed() {
        return getByCode(ActivationTokenStatusCode.USED);
    }
}
