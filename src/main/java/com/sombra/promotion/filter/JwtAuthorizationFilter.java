package com.sombra.promotion.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sombra.promotion.entity.JwtToken;
import com.sombra.promotion.entity.UserCredential;
import com.sombra.promotion.security.JwtAuthentication;
import com.sombra.promotion.service.JwtTokenService;
import com.sombra.promotion.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.sombra.promotion.util.Constants.*;
import static com.sombra.promotion.util.JwtTokenBuilder.getDecodedJWT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private  JwtTokenService jwtTokenService;
    @Autowired
    private  UserCredentialService userCredentialService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String servletPath = request.getServletPath();
        if (servletPath.equals(LOGIN_URL) || servletPath.equals(REFRESH_TOKEN_URL)) {
            filterChain.doFilter(request, response);
        } else {
            final String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_SPACE)) {
                try {
                    final DecodedJWT decodedJWT = getDecodedJWT(authorizationHeader);
                    final String email = decodedJWT.getSubject();
                    final UserCredential userCredential =  userCredentialService.getExistingByEmail(email);
                    userCredentialService.validateUserCredential(userCredential);
                    final JwtToken token = jwtTokenService
                            .getByAccessToken(authorizationHeader.substring(BEARER_SPACE.length()));
                    jwtTokenService.validateToken(token);

                    SecurityContextHolder.getContext().setAuthentication(new JwtAuthentication(userCredential, token));

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
}
