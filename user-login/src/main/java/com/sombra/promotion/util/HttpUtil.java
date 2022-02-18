package com.sombra.promotion.util;

import com.sombra.promotion.entity.JwtToken;
import com.sombra.promotion.entity.UserCredential;
import org.springframework.http.HttpHeaders;

import static com.sombra.promotion.service.impl.UserCredentialServiceImpl.getCurrentUserCredential;
import static com.sombra.promotion.util.Constants.BEARER_SPACE;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public final class HttpUtil {

    public static HttpHeaders getHttpHeaders() {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, APPLICATION_JSON_VALUE);
        headers.add(ACCEPT, APPLICATION_JSON_VALUE);
        return headers;
    }

    public static void setAuthorization(final HttpHeaders httpHeaders, final String authorizationToken) {
        httpHeaders.add(AUTHORIZATION, authorizationToken);
    }

    public static HttpHeaders getHttpAuthorizedHeaders() {
        final UserCredential currentUserCredential = getCurrentUserCredential();
        final JwtToken jwtToken = currentUserCredential.getJwtToken();
        final String accessToken = jwtToken.getAccessToken();
        final HttpHeaders httpHeaders = getHttpHeaders();
        setAuthorization(httpHeaders, BEARER_SPACE + accessToken);
        return httpHeaders;
    }
}
