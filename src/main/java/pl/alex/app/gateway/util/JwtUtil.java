package pl.alex.app.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final static String SECRET = "alex-secret:d3f4232d-ff8a-44b7-9a39-7b041a8018de";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    private Claims extractClaims(String token) {
        JwtParser parser = Jwts
                .parserBuilder()
                .setSigningKey(KEY)
                .build();

        return parser
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        return extractClaims(token).getExpiration().after(new Date());
    }
}
