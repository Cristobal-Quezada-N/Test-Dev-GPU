package usach.hackaton.gpu.config;

import org.springframework.context.annotation.Configuration;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Configuration
public class JwtUtil {

    private static final String SECRET = System.getProperty("SECRET");
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);
    private static final String dbName = System.getProperty("DB_NAME");
    // Este metodo crea un JWT con el nombre de usuario
    public String create(String email) {
        return JWT.create()
                .withSubject(email)
                .withIssuer(dbName)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(60)) // Modifica este valor para cambiar la duración del token
                ).sign(ALGORITHM);

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
    public String getEmail(String jwt){
        return JWT.require(ALGORITHM)
                .build()
                .verify(jwt)
                .getSubject();
    }
}
