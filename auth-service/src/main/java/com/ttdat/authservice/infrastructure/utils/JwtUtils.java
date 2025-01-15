package com.ttdat.authservice.infrastructure.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtils {
    private final ApplicationContext applicationContext;

    @Value("${application.security.jwt.access-token-validity-in-seconds}")
    private long accessTokenDurationInSeconds;

    @Value("${application.security.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenDurationInSeconds;

    public String generateAccessToken(UserDetails userDetails) {
        return generateJwtToken(userDetails, accessTokenDurationInSeconds);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return generateJwtToken(userDetails, refreshTokenDurationInSeconds);
    }

    private String generateJwtToken(UserDetails userDetails, long tokenDurationInSeconds) {
        JwtEncoder jwtEncoder = applicationContext.getBean(JwtEncoder.class);
        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .issuedAt(Instant.now())
                .id(UUID.randomUUID().toString())
                .issuer("CT553-Auth-Service")
                .expiresAt(Instant.now().plus(tokenDurationInSeconds, ChronoUnit.SECONDS))
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

    public String getUsername(String token) {
        return getJwtToken(token).getSubject();
    }

    private boolean isTokenExpired(String token) {
        return Objects.requireNonNull(getTokenExpiration(token)).isBefore(Instant.now());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return !isTokenExpired(token) && getUsername(token).equals(userDetails.getUsername());
    }
}
