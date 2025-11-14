package usach.hackaton.gpu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.AuthFactor;
import usach.hackaton.gpu.repositories.AuthFactorRepository;

@Service
@RequiredArgsConstructor
public class AuthFactorService {
    private final AuthFactorRepository authFactorRepository;

    public AuthFactor save(AuthFactor authFactor) {
        return authFactorRepository.save(authFactor);
    }
}
