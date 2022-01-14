package com.sombra.promotion.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Returns a 401 error code (Unauthorized) to the client.
 */

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Always returns a 401 error code to the client.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException arg2) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
