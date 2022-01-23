package com.sombra.promotion.filter;

import com.sombra.promotion.exception.BadRequestException;
import com.sombra.promotion.exception.UnauthorizedException;
import com.sombra.promotion.util.Constants;
import com.sombra.promotion.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.sombra.promotion.util.Constants.BEARER_SPACE;
import static com.sombra.promotion.util.Constants.TOKEN_IS_NOT_VALID;
import static com.sombra.promotion.util.HttpUtil.getHttpHeaders;
import static com.sombra.promotion.util.HttpUtil.setAuthorization;
import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@Component
public class TokenVerificationFilter extends OncePerRequestFilter {

    @Value("${url.user-login}")
    private String userLoginServiceUrl;
    private final RestTemplate restTemplate;

    public TokenVerificationFilter(@Qualifier("userLoginRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_SPACE)) {
            if (!JwtTokenUtil.isValidJWT(authorizationHeader)) {
                throw new UnauthorizedException(TOKEN_IS_NOT_VALID);
            }

            final HttpHeaders httpHeaders = getHttpHeaders();
            setAuthorization(httpHeaders, authorizationHeader);

            final RequestEntity<String> requestEntity = RequestEntity
                    .method(HttpMethod.POST, userLoginServiceUrl + Constants.URL.LOGIN_SERVICE.VERIFY_TOKEN)
                    .accept(MediaType.APPLICATION_JSON)
                    .headers(httpHeaders)
                    .body(authorizationHeader);

            final ResponseEntity<Boolean> responseEntity = restTemplate.exchange(requestEntity, Boolean.class);

            if (TRUE.equals(responseEntity.getBody())) {
                filterChain.doFilter(request, response);
            } else {
                log.error("Invalid token from {}", request.getRequestURI());
                throw new BadRequestException("Token is invalid!");
            }
        } else {
            throw new BadRequestException("Token is invalid!");
        }
    }
}
