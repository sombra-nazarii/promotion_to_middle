package com.sombra.promotion.filter;

import com.sombra.promotion.exception.BadRequestException;
import com.sombra.promotion.exception.UnauthorizedException;
import com.sombra.promotion.service.impl.RestTemplateService;
import com.sombra.promotion.util.JwtTokenUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.sombra.promotion.util.Constants.BEARER_SPACE;
import static com.sombra.promotion.util.Constants.TOKEN_IS_NOT_VALID;
import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AllArgsConstructor
@Slf4j
@Component
public class TokenVerificationFilter extends OncePerRequestFilter {

    private final RestTemplateService restTemplateService;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_SPACE)) {
            if (!JwtTokenUtil.isValidJWT(authorizationHeader)) {
                throw new UnauthorizedException(TOKEN_IS_NOT_VALID);
            }
            final ResponseEntity<Boolean> responseEntity = restTemplateService.isValidToken(authorizationHeader);

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
