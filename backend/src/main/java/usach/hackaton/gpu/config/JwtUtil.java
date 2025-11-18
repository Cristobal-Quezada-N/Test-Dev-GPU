package usach.hackaton.gpu.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private final Algorithm tokenAlgorithm;
    private final String dbName;

    public JwtUtil(@Value("${security.jwt.secret}") String jwtSignSecret,
        @Value("${DB_NAME:no-database}") String dbName) {
        this.tokenAlgorithm = Algorithm.HMAC256(jwtSignSecret);
        this.dbName = dbName;
    }

    // Este metodo crea un JWT con el nombre de usuario
    public String createToken(String userEmail) {
        return JWT.create().withSubject(userEmail).withIssuer(dbName).withIssuedAt(new Date())
            // Modifica este valor para cambiar la duración del token
            .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(60)))
            .sign(tokenAlgorithm);
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
