package usach.hackaton.gpu.service;

import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.AuthFactor;
import usach.hackaton.gpu.repositories.AuthFactorRepository;

@Service
public class AuthFactorService {
    private final AuthFactorRepository authFactorRepository;

    public AuthFactorService(AuthFactorRepository authFactorRepository) {
        this.authFactorRepository = authFactorRepository;
    }

    public void save (AuthFactor authFactor){ authFactorRepository.save(authFactor); }
}
