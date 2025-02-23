package booking.Services;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenService {

    private static final String SECRET_KEY = "mysecret"; // In production, use environment variable
    private static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 hours

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    public JwtTokenService() {
        this.algorithm = Algorithm.HMAC512(SECRET_KEY);
        this.verifier = JWT.require(algorithm)
                .withIssuer("cinema-city")
                .build();
    }

    public String generateToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuer("cinema-city")
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .sign(algorithm);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            String username = jwt.getSubject();
            return username.equals(userDetails.getUsername());
        } catch (Exception e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        try {
            return verifier.verify(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
