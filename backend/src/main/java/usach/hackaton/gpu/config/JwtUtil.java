package usach.hackaton.gpu.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtUtil {

    private final Algorithm ALGORITHM;
    private final String dbName;

    public JwtUtil(@Value("${security.jwt.secret}") String secret, @Value("${DB_NAME:no-database}") String dbName) {
        this.ALGORITHM = Algorithm.HMAC256(secret);
        this.dbName = dbName;
    }

    // Este metodo crea un JWT con el nombre de usuario
    public String create(String email) {
        return JWT.create().withSubject(email).withIssuer(dbName).withIssuedAt(new Date())
            // Modifica este valor para cambiar la duración del token
            .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(60))).sign(ALGORITHM);
    }

    // Este metodo verifica si un JWT es válido
    public boolean isValid(String jwt) {
        try {
            JWT.require(ALGORITHM).build().verify(jwt);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println("Token inválido: " + jwt);
            return false;
        }
    }

    // Este metodo extrae el nombre de usuario de un JWT
    public String getEmail(String jwt) {
        return JWT.require(ALGORITHM).build().verify(jwt).getSubject();
    }
}
