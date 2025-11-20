package usach.hackaton.gpu.service;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usach.hackaton.gpu.entities.AppUser;
import usach.hackaton.gpu.entities.AuthFactor;
import usach.hackaton.gpu.entities.AuthFactorTypeLookup;
import usach.hackaton.gpu.enums.AuthFactorCode;
import usach.hackaton.gpu.repositories.AuthFactorRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthFactorService {
    private final AuthFactorRepository authFactorRepository;
    private final AuthFactorTypeLookupService authFactorTypeLookupService;

    public AuthFactor save(AuthFactor authFactor) {
        return authFactorRepository.save(authFactor);
    }

    public AuthFactor createRegisterFactor(AppUser user) {
        // Crear factor de autentificacion para registro
        AuthFactorTypeLookup registerType = authFactorTypeLookupService.getByCode(AuthFactorCode.REGISTER);
        AuthFactor factor = AuthFactor.builder()
            .user(user)
            .type(registerType)
            .used(false)
            .creationDate(LocalDateTime.now())
            .expirationDate(LocalDateTime.now().plusHours(24))
            .build();

        return save(factor);
    }

    @Transactional(readOnly = true)
    public AuthFactor getAuthFactorByUserId(UUID userId, AuthFactorCode authFactorCode) {
        return authFactorRepository.findByUserId(userId).stream()
            .filter(authFactor -> authFactorCode.name().equals(authFactor.getType().getCode())).findFirst()
            .orElseThrow(
                () -> new IllegalStateException(
                    "Factor not found: factor =  " + authFactorCode.name() + " user = " + userId
                )
            );
    }

    @Transactional
    public void markEmailAsVerified(AppUser user) {
        AuthFactor emailFactor = getAuthFactorByUserId(user.getId(), AuthFactorCode.REGISTER);

        emailFactor.setUsed(true);
        save(emailFactor);

        log.debug("Email Factor marked as verified for user: {}", user.getEmail());
    }

    @Transactional
    public void delete(Long id) {
        authFactorRepository.deleteById(id);
    }

    public boolean userHasValidAuthFactor(AppUser user, AuthFactorCode authFactorCode) {
        return getAuthFactorByUserId(user.getId(), authFactorCode).isValid();
    }
}
