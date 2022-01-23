package com.sombra.promotion.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sombra.promotion.entity.User;
import com.sombra.promotion.security.UserAuthentication;
import com.sombra.promotion.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.sombra.promotion.util.Constants.*;
import static com.sombra.promotion.util.JwtTokenUtil.getDecodedJWT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_SPACE)) {
            try {
                final DecodedJWT decodedJWT = getDecodedJWT(authorizationHeader);
                final Long userCredentialId = decodedJWT.getClaim(USER_ID).asLong();
                final User user = userService.getExistingByUserCredentialId(userCredentialId);

                SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(user));
                filterChain.doFilter(request, response);
            } catch (Exception exception) {
                log.error("JwtAuthorizationFilter: {}", exception.getMessage());
                response.setHeader(ERROR, exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put(MESSAGE, exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
