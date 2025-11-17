package usach.hackaton.gpu.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.ActivationTokenStatus;
import usach.hackaton.gpu.repositories.ActivationTokenStatusRepository;

@Service
@RequiredArgsConstructor
public class ActivationTokenStatusService {
    private final ActivationTokenStatusRepository activationTokenStatusRepository;

    public Optional<ActivationTokenStatus> findByCode(String code) {
        return activationTokenStatusRepository.findByCode(code);
    }

    private ActivationTokenStatus getByCode(String code) {
        return findByCode(code).orElseThrow(() -> new IllegalStateException(code + "status code not found"));
    }

    public ActivationTokenStatus getPending() {
        return getByCode("PENDING");
    }
}
