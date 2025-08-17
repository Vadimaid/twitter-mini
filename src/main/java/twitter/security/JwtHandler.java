package twitter.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import twitter.configuration.Component;
import twitter.configuration.Injection;
import twitter.configuration.Value;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtHandler {

    @Value(key = "jwt.token.secret")
    private String tokenSecret;

    @Value(key = "jwt.token.lifetime")
    private Long tokenLifetime;

    @Injection
    public JwtHandler() {}

    public String generateToken(String username) {
        Date now = new Date();
        Date expiredAt = new Date(now.getTime() + this.tokenLifetime);

        return Jwts
                .builder()
                .setIssuedAt(now)
                .setExpiration(expiredAt)
                .setSubject(username)
                .signWith(this.getSighInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(this.getSighInKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(this.getSighInKey()).build().parse(token);
            return true;
        } catch (ExpiredJwtException ex) {
            System.out.println("Token expired");
        } catch (MalformedJwtException ex) {
            System.out.println("Token invalid");
        } catch (SignatureException ex) {
            System.out.println("Token signature incorrect");
        } catch (IllegalArgumentException ex) {
            System.out.println("Token must contain claims");
        }
        return false;
    }

    private Key getSighInKey() {
        byte[] keyBytes = Base64.getDecoder().decode(this.tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
