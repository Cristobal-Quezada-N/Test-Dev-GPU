package usach.hackaton.gpu.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {
    private final Algorithm tokenAlgorithm;
    private final String issuer;
    private final long tokenExpirationMinutes;
    private final JWTVerifier jwtVerifier;

    public JwtUtil(
        @Value("${security.jwt.secret}") String jwtSignSecret,
        @Value("${security.jwt.issuer}") String issuer,
        @Value("${security.jwt.expiration-minutes:60}") long tokenExpirationMinutes) {
        this.tokenAlgorithm = Algorithm.HMAC256(jwtSignSecret);
        this.issuer = issuer;
        this.tokenExpirationMinutes = tokenExpirationMinutes;
        this.jwtVerifier = JWT.require(tokenAlgorithm)
            .withIssuer(issuer)
            .build();
    }

    // Este metodo crea un JWT con el nombre de usuario
    public String createToken(String userEmail) {
        if (userEmail == null || userEmail.isBlank()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        Instant instantTime = Instant.now();
        Instant expirationTime = instantTime.plus(tokenExpirationMinutes, ChronoUnit.MINUTES);

        String jwtToken = JWT.create()
            .withSubject(userEmail)
            .withIssuer(issuer)
            .withIssuedAt(Date.from(instantTime))
            .withExpiresAt(Date.from(expirationTime))
            .sign(tokenAlgorithm);

        log.debug("[JWT] Created token for user: {} (expires in {} minutes)", userEmail, tokenExpirationMinutes);
        return jwtToken;
    }

    // Este metodo verifica si un JWT es válido
    public boolean isValidToken(String jwtToken) {
        if (jwtToken == null || jwtToken.isBlank()) {
            return false;
        }

        try {
            jwtVerifier.verify(jwtToken);
            return true;
        } catch (JWTVerificationException e) {
            log.debug("[JWT] Invalid token: {}", e.getMessage());
            return false;
        }
    }

    // Extrae el email del usuario desde un JWT
    public String getEmail(String jwtToken) {
        if (!isValidToken(jwtToken)) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            log.error("[JWT] Failed to extract user email from token: {}", e.getMessage(), e);
            throw e;
        }
    }
}
