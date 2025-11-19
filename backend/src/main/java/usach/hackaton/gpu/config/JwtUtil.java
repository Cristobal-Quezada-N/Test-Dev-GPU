package usach.hackaton.gpu.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
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

    public JwtUtil(
        @Value("${security.jwt.secret}") String jwtSignSecret,
        @Value("${security.jwt.issuer}") String issuer,
        @Value("${security.jwt.expiration-minutes:60}") long tokenExpirationMinutes) {
        this.tokenAlgorithm = Algorithm.HMAC256(jwtSignSecret);
        this.issuer = issuer;
        this.tokenExpirationMinutes = tokenExpirationMinutes;
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
        try {
            JWT.require(tokenAlgorithm).build().verify(jwtToken);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println("Token inválido: " + jwtToken);
            return false;
        }
    }

    // Este metodo extrae el nombre de usuario de un JWT
    public String getEmail(String jwtToken) {
        return JWT.require(tokenAlgorithm).build().verify(jwtToken).getSubject();
    }
}
