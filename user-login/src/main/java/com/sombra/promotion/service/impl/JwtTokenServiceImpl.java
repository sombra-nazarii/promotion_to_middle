package com.sombra.promotion.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.sombra.promotion.dto.anonymous.JwtTokenDTO;
import com.sombra.promotion.dto.anonymous.LoginUserDTO;
import com.sombra.promotion.entity.JwtToken;
import com.sombra.promotion.entity.UserCredential;
import com.sombra.promotion.exception.BadRequestException;
import com.sombra.promotion.exception.UnauthorizedException;
import com.sombra.promotion.mapper.JwtTokenMapper;
import com.sombra.promotion.repository.JwtTokenRepository;
import com.sombra.promotion.service.JwtTokenService;
import com.sombra.promotion.service.UserCredentialService;
import com.sombra.promotion.util.JwtTokenBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

import static com.sombra.promotion.util.Constants.*;
import static com.sombra.promotion.util.JwtTokenBuilder.getDecodedJWT;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.isNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenBuilder jwtTokenBuilder;
    private final UserCredentialService userCredentialService;
    private final JwtTokenMapper jwtTokenMapper;

    public static final String TOKEN_IS_NOT_VALID = "Token is not valid";
    public static final String TOKEN_HAD_EXPIRED = "Token had expired";
    public static final String TOKEN_NOT_EXISTS = "Token not exists";

    @Override
    @Transactional
    public JwtTokenDTO login(final LoginUserDTO loginUserDTO) {
        final UserCredential userCredential = userCredentialService.getExistingByEmail(loginUserDTO.getEmail());
        userCredentialService.validateUserCredential(userCredential);
        userCredentialService.validatePassword(loginUserDTO.getPassword(), userCredential.getPassword());
        updateUserLastLogged(userCredential);

        final JwtToken token = getJwtToken(userCredential);
        return jwtTokenMapper.toDTO(token);
    }

    @Override
    @Transactional
    public JwtTokenDTO refreshToken(final String bearerRefreshToken) {
        if (bearerRefreshToken != null && bearerRefreshToken.startsWith(BEARER_SPACE)) {
            try {
                final DecodedJWT decodedJWT = getDecodedJWT(bearerRefreshToken);
                final String email = decodedJWT.getSubject();
                final UserCredential userCredential = userCredentialService.getExistingByEmail(email);
                final String refreshToken = bearerRefreshToken.substring(BEARER_SPACE.length());
                final JwtToken jwtToken = jwtTokenRepository.getByRefreshToken(refreshToken);
                final String accessToken = jwtTokenBuilder.getAccessToken(userCredential, REFRESH_TOKEN_URL);

                jwtToken.setAccessToken(accessToken).setDeleted(FALSE).setValid(TRUE);
                jwtTokenRepository.save(jwtToken);

                return jwtTokenMapper.toDTO(jwtToken);
            } catch (Exception exception) {
                throw new BadRequestException(COULD_NOT + GENERATE + SPACE + REFRESH_TOKEN + COLON + SPACE + exception.getMessage());
            }
        } else {
            throw new BadRequestException(REFRESH_TOKEN + IS_MISSING);
        }
    }

    @Override
    @Transactional
    public void logout() {
        final JwtToken token = getCurrentJWT();
        invalidateToken(token);
    }

    private void invalidateToken(final JwtToken token) {
        if (Objects.isNull(token)) {
            throw new BadRequestException(TOKEN + IS_MISSING);
        }
        token.setValid(FALSE).setDeleted(TRUE);

        jwtTokenRepository.save(token);
    }

    @Override
    public void validateToken(final JwtToken token) {
        if (!jwtTokenBuilder.isValidJjwSignature(token)) {
            throw new UnauthorizedException(TOKEN_IS_NOT_VALID);
        }
        if (isNull(token)) {
            throw new UnauthorizedException(TOKEN_NOT_EXISTS);
        }
        if (!token.isValid()) {
            throw new UnauthorizedException(TOKEN_IS_NOT_VALID);
        }
        if (token.isDeleted()) {
            throw new UnauthorizedException(TOKEN_IS_NOT_VALID);
        }
        if (token.getAccessTokenExpirationDateTime().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            throw new UnauthorizedException(TOKEN_HAD_EXPIRED);
        }
        if (token.getRefreshTokenExpirationDateTime().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            throw new UnauthorizedException(TOKEN_HAD_EXPIRED);
        }
    }

    @Override
    public JwtToken getByAccessToken(final String accessToken) {
        if (isNull(accessToken)) {
            throw new BadRequestException(ACCESS + SPACE + TOKEN + IS_MISSING);
        }
        return jwtTokenRepository.getByAccessToken(accessToken);
    }

    @Override
    public JwtToken getCurrentJWT() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (JwtToken) authentication.getCredentials();
    }

    @Override
    public Boolean verifyAccessToken(final String bearerAccessToken) {
        if (isNull(bearerAccessToken) || bearerAccessToken.startsWith(BEARER_SPACE)){
            return FALSE;
        }
        final String accessToken = bearerAccessToken.substring(BEARER_SPACE.length());
        final JwtToken jwtToken = getCurrentJWT();
        final Boolean isValidJWT = isValidJWT(jwtToken);

        if (isValidJWT){
            return jwtToken.getAccessToken().equals(accessToken);
        }else {
            return FALSE;
        }
    }

    private JwtToken createJWT(final UserCredential userCredential) {
        final String accessToken = jwtTokenBuilder.getAccessToken(userCredential, LOGIN_URL);
        final String refreshToken = jwtTokenBuilder.getRefreshToken(userCredential, LOGIN_URL);
        final JwtToken jwtToken = JwtToken.createInstance(
                userCredential,
                accessToken,
                refreshToken,
                jwtTokenBuilder.getAccessTokenExpirationSeconds(),
                jwtTokenBuilder.getRefreshTokenExpirationSeconds());

        userCredential.setJwtToken(jwtToken);
        return jwtTokenRepository.save(jwtToken);
    }

    private JwtToken renewJWT(final UserCredential userCredential) {
        final JwtToken jwtToken;
        final JwtToken existingJwt = userCredential.getJwtToken();
        if (jwtTokenBuilder.isValidJjwSignature(existingJwt)) {
            jwtToken = existingJwt;
            existingJwt.setValid(TRUE).setDeleted(FALSE);
        } else {
            existingJwt.setDeleted(TRUE).setValid(FALSE);
            jwtTokenRepository.save(existingJwt);
            jwtToken = createJWT(userCredential);
        }
        return jwtToken;
    }

    private JwtToken getJwtToken(final UserCredential userCredential) {
        final JwtToken token;
        if (isNull(userCredential.getJwtToken())) {
            token = createJWT(userCredential);
        } else {
            token = renewJWT(userCredential);
        }
        return token;
    }

    private void updateUserLastLogged(final UserCredential userCredential) {
        final LocalDateTime currentDate = LocalDateTime.now(ZoneOffset.UTC);
        userCredential.setLastLogged(currentDate);
    }

    private Boolean isValidJWT(final JwtToken jwtToken) {
        Boolean isValid;
        try {
            validateToken(jwtToken);
            isValid = TRUE;
        } catch (Exception e){
            isValid = FALSE;
        }
        return isValid;
    }
}
