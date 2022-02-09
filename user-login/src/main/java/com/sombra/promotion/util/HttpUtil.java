package com.sombra.promotion.util;

import org.springframework.http.HttpHeaders;

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
}
