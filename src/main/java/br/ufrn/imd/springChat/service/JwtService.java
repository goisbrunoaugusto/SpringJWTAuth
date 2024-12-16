package br.ufrn.imd.springChat.service;

import br.ufrn.imd.springChat.model.UserDetailsImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class JwtService {
    private final String ISSUER = System.getenv("ISSUER");
    private final String SECRET_KEY = System.getenv("SECRET_KEY");

    private final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    public String validateToken(String token) {
        return JWT.require(algorithm)
                .withIssuer(ISSUER)
                .build()
                .verify(token)
                .getSubject();
    }

    public String generateToken(UserDetailsImpl user) {
        try{
            String token = JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getUsername())
                    .withIssuedAt(creationDate())
                    .withExpiresAt(expirationDate())
                    .sign(algorithm);
            return token;
        } catch (Exception e){
            throw new RuntimeException("Error generating token");
        }
    }

    private Instant creationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Recife")).toInstant();
    }
    private Instant expirationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Recife")).plusHours(4).toInstant();
    }

}
