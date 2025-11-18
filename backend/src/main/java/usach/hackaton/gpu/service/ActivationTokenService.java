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

    @Transactional
    public ActivationToken markAsExpired(ActivationToken token) {
        ActivationTokenStatus expiredStatus = activationTokenStatusService.getExpired();
        token.setStatus(expiredStatus);
        return save(token);
    }

    @Transactional
    public ActivationToken markAsRevoked(ActivationToken token) {
        ActivationTokenStatus revokedStatus = activationTokenStatusService.getRevoked();
        token.setStatus(revokedStatus);
        return save(token);
    }

    @Transactional
    public ActivationToken markAsUsed(ActivationToken token) {
        ActivationTokenStatus usedStatus = activationTokenStatusService.getUsed();
        token.setStatus(usedStatus);
        token.setUsedAt(LocalDateTime.now());
        return save(token);
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
    public ActivationToken validateAndUse(String stringToken) {
        Optional<ActivationToken> optionalToken = findByToken(stringToken);

        if (optionalToken.isEmpty()) {
            return null;
        }

        ActivationToken token = optionalToken.get();

        if (!token.isValid()) {
            if (token.isPending() && token.hasExpired()) {
                markAsExpired(token);
            }
            return null;
        }

        return markAsUsed(token);
    }
}
