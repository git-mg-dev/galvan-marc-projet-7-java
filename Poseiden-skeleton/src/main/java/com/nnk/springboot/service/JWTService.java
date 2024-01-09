package com.nnk.springboot.service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nnk.springboot.domain.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    /**
     * Generates a new token for authenticated user
     * @param user authenticated
     * @return Encrypted token
     */
    public String generateToken(User user, String role) {
        Instant now = Instant.now();
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.DAYS))
                .subject(user.getUsername())
                .claim("role", role)
                .build();

        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claimsSet);
        return jwtEncoder().encode(jwtEncoderParameters).getTokenValue();
    }

    /**
     * Retrieves username from encrypted token
     * @param token
     * @return username
     */
    public String extractUsername(String token) {
        return extractClaim(token, "sub");
    }

    /**
     * Retrieves cookie expiration date from encrypted token
     * @param token
     * @return expiration date
     */
    public Date extractExpiration(String token) {
        Instant issuedAt = extractClaim(token, "exp");
        return Date.from(issuedAt);
    }

    /**
     * Retrieves user role from encrypted token
     * @param token
     * @return user role
     */
    public String extractRole(String token) {
        String role = extractClaim(token, "role");
        if(role == null) {
            role = "";
        }
        return role;
    }

    /**
     * Extracts a specific claim from encrypted token
     * @param token
     * @param claim name of claim to extract
     * @return retrieved data
     * @param <T>
     */
    private <T> T extractClaim(String token, String claim) {
        Jwt jwt = extractAllClaims(token);
        return jwt.getClaim(claim);
    }

    /**
     * Extracts all claims from encrypted token
     * @param token
     * @return uncrypted token
     */
    private Jwt extractAllClaims(String token) {
        return jwtDecoder().decode(token);
    }

    /**
     * Check if token is valid based on expiration date only
     * @param token
     * @return true or false
     */
    public Boolean validateToken(String token) {
        boolean isTokenExpired = extractExpiration(token).before(new Date());
        return !isTokenExpired;
    }

    /**
     * Checks if token is valid
     * @param token to verify
     * @param userDetails authenticated user
     * @return true or false
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);

        boolean isSameUser = username.equals(userDetails.getUsername());
        boolean isTokenExpired = extractExpiration(token).before(new Date());

        return (isSameUser && !isTokenExpired);
    }

    /**
     * Checks if a cookie exists for this app
     * @param request
     * @return role of user in cookie
     */
    public String checkCookies(HttpServletRequest request) {
        String jwtToken = "";

        if(request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("token-poseidon")) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }

            if(!jwtToken.isEmpty()) {
                if (validateToken(jwtToken)) {
                    return extractRole(jwtToken);
                }
            }
        }

        return "";
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
