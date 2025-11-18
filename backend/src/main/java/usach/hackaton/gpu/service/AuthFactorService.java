package usach.hackaton.gpu.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usach.hackaton.gpu.entities.AppUser;
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

    public AuthFactor createRegisterFactor(AppUser user) {
        AuthFactorTypeLookup registerType = authFactorTypeLookupService.getByCode(AuthFactorCode.REGISTER);
        AuthFactor factor = AuthFactor.builder()
            .user(user)
            .type(registerType)
            .used(true)
            .creationDate(LocalDateTime.now())
            .expirationDate(LocalDateTime.now().plusYears(1))
            .build();
        return save(factor);
    }

    @Transactional
    public void delete(Long id) {
        authFactorRepository.deleteById(id);
    }

    public boolean userHasValidAuthFactor(AppUser user) {
        List<AuthFactor> authFactors = authFactorRepository.findByUserId(user.getId());
        return authFactors.stream().anyMatch(authFactor -> authFactor.isActive());
    }
}
