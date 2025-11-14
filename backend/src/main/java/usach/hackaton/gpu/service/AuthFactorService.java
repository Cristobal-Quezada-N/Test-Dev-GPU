package usach.hackaton.gpu.service;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.AuthFactor;
import usach.hackaton.gpu.entities.AuthFactorTypeLookup;
import usach.hackaton.gpu.enums.AuthFactorCode;
import usach.hackaton.gpu.repositories.AuthFactorRepository;

@Service
@RequiredArgsConstructor
public class AuthFactorService {
    private final AuthFactorRepository authFactorRepository;
    private final AuthFactorTypeLookupService authFactorTypeLookupService;

    public AuthFactor save(AuthFactor authFactor) {
        return authFactorRepository.save(authFactor);
    }

    public AuthFactor createRegisterFactor(UUID userId) {
        AuthFactorTypeLookup registerType = authFactorTypeLookupService.getByCode(AuthFactorCode.REGISTER);
        AuthFactor factor = AuthFactor.builder()
            .userId(userId)
            .type(registerType)
            .used(true)
            .creationDate(LocalDateTime.now())
            .expirationDate(LocalDateTime.now().plusYears(1))
            .build();
        return save(factor);
    }
}
