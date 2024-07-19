package com.sum.chatter.service.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtBuilder {

    private final SecretKey key;

    private static final long EXPIRATION = 60 * 1000 * 60; // 1Hour

    public JwtBuilder(@Value("${jwt.secret}") String jwtSecret) {
        key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String createJwt(JwtInfo info) {
        Duration expirationDuration = Duration.ofHours(1);

        return Jwts.builder()
                .id(info.id().toString())
                .expiration(Date.from(Instant.now().plusMillis(expirationDuration.toMillis())))
                .signWith(key)
                .compact();
    }

    public JwtInfo decryptJwt(String jwtToken) {
        try {
            String id = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwtToken).getPayload().getId();

            return new JwtInfo(Long.parseLong(id));
        } catch (RuntimeException exception) {
            return new JwtInfo(null);
        }
    }

}
