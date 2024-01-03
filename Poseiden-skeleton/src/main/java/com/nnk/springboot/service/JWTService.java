package com.nnk.springboot.service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nnk.springboot.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JWTService {
    @Value("${app.jwt.secret}")
    private String jwtKey;

    public String generateToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(user.getUsername())
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claimsSet);
        return jwtEncoder().encode(jwtEncoderParameters).getTokenValue();
    }

    public String extractUsername(String token) {
        return extractClaim(token, "sub");
    }

    public Date extractExpiration(String token) {
        Instant issuedAt = extractClaim(token, "exp");
        return Date.from(issuedAt);
    }

    public <T> T extractClaim(String token, String claim) {
        Jwt jwt = extractAllClaims(token);
        return jwt.getClaim(claim);
    }

    private Jwt extractAllClaims(String token) {
        return jwtDecoder().decode(token);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);

        boolean isSameUser = username.equals(userDetails.getUsername());
        boolean isTokenExpired = extractExpiration(token).before(new Date());

        return (isSameUser && !isTokenExpired);
    }

    public Boolean validateToken(String token, User user) {
        String username = extractUsername(token);

        boolean isSameUser = username.equals(user.getUsername());
        boolean isTokenExpired = extractExpiration(token).before(new Date());

        return (isSameUser && !isTokenExpired);
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(this.jwtKey.getBytes()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(this.jwtKey.getBytes(), 0, this.jwtKey.getBytes().length, "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }
}
