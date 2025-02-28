package com.ttdat.userservice.infrastructure.utils;

import com.ttdat.userservice.domain.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final ApplicationContext applicationContext;

    @Value("${application.security.jwt.access-token-validity-in-seconds}")
    private long accessTokenDurationInSeconds;

    @Value("${application.security.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenDurationInSeconds;

    public String generateAccessToken(User user) {
        return generateJwtToken(user, accessTokenDurationInSeconds);
    }

    public String generateRefreshToken(User user) {
        return generateJwtToken(user, refreshTokenDurationInSeconds);
    }

    private String generateJwtToken(User user, long tokenDurationInSeconds) {
        JwtEncoder jwtEncoder = applicationContext.getBean(JwtEncoder.class);
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(user.getUserId().toString())
                .issuedAt(Instant.now())
                .id(UUID.randomUUID().toString())
                .issuer("https://agrohub-user-service.ttdat.com")
                .expiresAt(Instant.now().plus(tokenDurationInSeconds, ChronoUnit.SECONDS))
                .claim("role", user.getRole().getRoleName())
                .claim("warehouse_id", user.getWarehouseId())
                .build();
        JwsHeader jwsHeader = JwsHeader.with(SignatureAlgorithm.RS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();
    }

    private Jwt getJwtToken(String token) {
        JwtDecoder jwtDecoder = applicationContext.getBean(JwtDecoder.class);
        return jwtDecoder.decode(token);
    }

    public String getTokenId(String token) {
        return getJwtToken(token).getId();
    }

    public Instant getTokenExpiration(String token) {
        return getJwtToken(token).getExpiresAt();
    }

    public String getUserId(String token) {
        return getJwtToken(token).getSubject();
    }

//    private boolean isTokenExpired(String token) {
//        return Objects.requireNonNull(getTokenExpiration(token)).isBefore(Instant.now());
//    }
//
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        return !isTokenExpired(token) && getUserId(token).equals(userDetails.getUserId());
//    }
}
