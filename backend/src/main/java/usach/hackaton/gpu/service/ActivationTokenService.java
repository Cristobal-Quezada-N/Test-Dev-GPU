package usach.hackaton.gpu.service;

import jakarta.transaction.Transactional;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import usach.hackaton.gpu.entities.ActivationToken;
import usach.hackaton.gpu.entities.ActivationTokenStatus;
import usach.hackaton.gpu.entities.AppUser;
import usach.hackaton.gpu.enums.ActivationTokenStatusCode;
import usach.hackaton.gpu.repositories.ActivationTokenRepository;

@Service
@RequiredArgsConstructor
public class ActivationTokenService {
    private final ActivationTokenRepository activationTokenRepository;
    private final ActivationTokenStatusService activationTokenStatusService;

    @Transactional
    public void delete(ActivationToken activationToken) {
        activationTokenRepository.delete(activationToken);
    }

    public Optional<ActivationToken> findByToken(String token) {
        return activationTokenRepository.findByToken(token);
    }

    private boolean isTokenValid(ActivationToken token) {
        if (!ActivationTokenStatusCode.PENDING.name().equals(token.getStatus().getCode())) {
            return false;
        }

        if (token.getExpirationDate().isBefore(LocalDateTime.now())) {
            return false;
        }

        return true;
    }

    @Transactional
    public void markAsExpired(ActivationToken token) {
        ActivationTokenStatus expiredStatus = activationTokenStatusService.getExpired();
        token.setStatus(expiredStatus);
        save(token);
    }

    @Transactional
    public void markAsRevoked(ActivationToken token) {
        ActivationTokenStatus revokedStatus = activationTokenStatusService.getRevoked();
        token.setStatus(revokedStatus);
        save(token);
    }

    @Transactional
    public void markAsUsed(ActivationToken token) {
        ActivationTokenStatus usedStatus = activationTokenStatusService.getUsed();
        token.setStatus(usedStatus);
        token.setUsedAt(LocalDateTime.now());
        save(token);
    }

    @Transactional
    public ActivationToken createActivationToken(AppUser user) {
        ActivationTokenStatus pendingStatus = activationTokenStatusService.getPending();
        String newToken = generateToken();
        ActivationToken activationToken = ActivationToken.builder()
            .user(user)
            .token(newToken)
            .status(pendingStatus)
            .creationDate(LocalDateTime.now())
            .expirationDate(LocalDateTime.now().plusHours(24))
            .build();
        return activationTokenRepository.save(activationToken);
    }

    private static String generateToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    @Transactional
    private ActivationToken save(ActivationToken activationToken) {
        return activationTokenRepository.save(activationToken);
    }

    @Transactional
    public boolean validateAndUse(String stringToken) {
        Optional<ActivationToken> optionalToken = findByToken(stringToken);

        if (optionalToken.isEmpty()) {
            return false;
        }

        ActivationToken token = optionalToken.get();

        if (!isTokenValid(token)) {
            if (!ActivationTokenStatusCode.PENDING.name().equals(token.getStatus().getCode())
                && token.getExpirationDate().isBefore(LocalDateTime.now())) {
                markAsExpired(token);
            }
            return false;
        }

        markAsUsed(token);
        return true;
    }
}
