package com.sombra.promotion.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import static com.sombra.promotion.util.Constants.BEARER_SPACE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Component
public final class JwtTokenUtil {

    public static DecodedJWT getDecodedJWT(final String encodedToken) {
        final String token = encodedToken.substring(BEARER_SPACE.length());
        final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        final JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public static boolean isValidJWT(final String token) {
        final Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        final JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            verifier.verify(token);
        } catch (Exception exception) {
            return FALSE;
        }
        return TRUE;
    }
}
