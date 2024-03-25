package com.tutran.springblog.api.security;

import com.tutran.springblog.api.exception.CommentNotBelongingToPostException;
import com.tutran.springblog.api.exception.JwtTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value(value = "${app.jwt-secret}")
    private String jwtSecret;

    @Value(value = "${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    public String generateToken(Authentication authentication) {
        var currentDate = new Date();
        var expiredDate = new Date(currentDate.getTime() + jwtExpirationDate);

        var username = authentication.getName();

        return Jwts.builder()
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expiredDate)
                .signWith(key())
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) key()).build().parse(token);
            return true;
        } catch (MalformedJwtException malformedJwtException) {
            throw new JwtTokenException("Invalid JWT token");
        } catch (ExpiredJwtException expiredJwtException) {
            throw new JwtTokenException("Expired JWT token");
        } catch (UnsupportedJwtException unsupportedJwtException) {
            throw new JwtTokenException("Unsupported JWT token");
        } catch (SignatureException signatureException) {
            throw new CommentNotBelongingToPostException("Signature JWT does not match computed one");
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new JwtTokenException("JWT claims string is null or empty");
        }
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
}
