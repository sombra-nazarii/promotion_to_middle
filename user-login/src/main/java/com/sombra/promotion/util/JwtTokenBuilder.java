package com.sombra.promotion.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sombra.promotion.entity.JwtToken;
import com.sombra.promotion.entity.UserCredential;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

import static com.sombra.promotion.util.Constants.BEARER_SPACE;
import static com.sombra.promotion.util.Constants.ROLES;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Component
public final class JwtTokenBuilder {

    //    @Value("${token.expiration.seconds}")
    private final Long accessTokenExpirationSeconds = 1000000L;
    private final Long refreshTokenExpirationSeconds = 1000000L;

    public String getAccessToken(final UserCredential userCredential,
                                 final String issuerURL) {
        final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return JWT.create()
                .withSubject(userCredential.getEmail())
                .withExpiresAt(new Date((System.currentTimeMillis() + accessTokenExpirationSeconds)))
                .withIssuer(issuerURL)
                .withClaim(ROLES, userCredential.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String getRefreshToken(final UserCredential userCredential,
                                  final String issuerURL) {
        final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return JWT.create()
                .withSubject(userCredential.getEmail())
                .withExpiresAt(new Date((System.currentTimeMillis() + refreshTokenExpirationSeconds)))
                .withIssuer(issuerURL)
                .sign(algorithm);
    }

    public static DecodedJWT getDecodedJWT(final String encodedToken) {
        final String token = encodedToken.substring(BEARER_SPACE.length());
        final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        final JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public Long getAccessTokenExpirationSeconds() {
        return accessTokenExpirationSeconds;
    }

    public Long getRefreshTokenExpirationSeconds() {
        return accessTokenExpirationSeconds;
    }

    public boolean isValidJjwSignature(final JwtToken token) {
        final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        final JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            verifier.verify(token.getAccessToken());
            verifier.verify(token.getRefreshToken());
        } catch (Exception exception) {
            return FALSE;
        }
        return TRUE;
    }
}
