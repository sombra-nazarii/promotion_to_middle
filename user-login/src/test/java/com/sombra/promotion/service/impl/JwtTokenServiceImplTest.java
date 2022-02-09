package com.sombra.promotion.service.impl;

import com.sombra.promotion.dto.anonymous.LoginUserDTO;
import com.sombra.promotion.entity.JwtToken;
import com.sombra.promotion.entity.UserCredential;
import com.sombra.promotion.exception.BadRequestException;
import com.sombra.promotion.exception.UnauthorizedException;
import com.sombra.promotion.mapper.JwtTokenMapper;
import com.sombra.promotion.repository.JwtTokenRepository;
import com.sombra.promotion.service.UserCredentialService;
import com.sombra.promotion.util.JwtTokenBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static com.sombra.promotion.service.impl.JwtTokenServiceImpl.*;
import static com.sombra.promotion.util.Constants.*;
import static com.sombra.promotion.util.Constants.URL.REFRESH_TOKEN_URL;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JwtTokenServiceImplTest {

    @Mock
    private UserCredentialService userCredentialServiceMock;
    @Mock
    private JwtTokenRepository jwtTokenRepositoryMock;
    @Mock
    private JwtTokenBuilder jwtTokenBuilderMock;
    @Mock
    private JwtTokenMapper jwtTokenMapperMock;
    @Spy
    @InjectMocks
    private JwtTokenServiceImpl sut;
    private JwtTokenBuilder jwtTokenBuilder;

    private LoginUserDTO loginUserDTO;
    private UserCredential userCredential;
    private JwtToken jwtToken;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
        jwtTokenBuilder = new JwtTokenBuilder();
        loginUserDTO = new LoginUserDTO().setPassword("kodpizda1234").setEmail("john@john.com");
        userCredential = new UserCredential()
                .setDeleted(FALSE)
                .setEnabled(TRUE)
                .setPassword("$2a$10$wIQYCdbRtEaJNsU71a2PTu3MzNqLdbJmQHcSCvAcOb33WLUUPOrLC");
        jwtToken = new JwtToken()
                .setValid(TRUE)
                .setDeleted(FALSE)
                .setAccessToken("AccessToken")
                .setRefreshToken("RefreshToken")
                .setUserCredential(userCredential);
    }

    @Test
    void login_clearCaseFirstTimeLogin_correctBehaviour() {

        // SETUP
        when(userCredentialServiceMock.getExistingByEmail(anyString())).thenReturn(userCredential);

        // ACT
        sut.login(loginUserDTO);

        // VERIFY
        verify(jwtTokenMapperMock, only()).toDTO(any());

    }

    @Test
    void login_clearCaseLoginAgain_correctBehaviour() {

        // SETUP
        userCredential.setJwtToken(jwtToken);
        when(userCredentialServiceMock.getExistingByEmail(anyString())).thenReturn(userCredential);

        // ACT
        sut.login(loginUserDTO);

        // VERIFY
        verify(jwtTokenMapperMock, only()).toDTO(any());

    }

    @Test
    void login_clearCaseLoginAgainValidSignature_correctBehaviour() {

        // SETUP
        userCredential.setJwtToken(jwtToken);
        when(jwtTokenBuilderMock.isValidJWT(any(JwtToken.class))).thenReturn(TRUE);
        when(userCredentialServiceMock.getExistingByEmail(anyString())).thenReturn(userCredential);

        // ACT
        sut.login(loginUserDTO);

        // VERIFY
        verify(jwtTokenMapperMock, only()).toDTO(any());

    }

    @Test
    void login_missingUser_throwsUnauthorizedException() {

        // SETUP
        when(userCredentialServiceMock.getExistingByEmail(anyString())).thenReturn(null);
        doThrow(new UnauthorizedException(NOT_AUTHORIZED)).when(userCredentialServiceMock).validateUserCredential(null);

        // VERIFY
        final UnauthorizedException resultException = assertThrows(UnauthorizedException.class,
                () -> sut.login(loginUserDTO));
        assertEquals(NOT_AUTHORIZED, resultException.getMessage());
        verify(jwtTokenMapperMock, never()).toDTO(any());

    }

    @Test
    void logout_clearCase_correctBehaviour() {

        // SETUP
        doReturn(new JwtToken()).when(sut).getCurrentJWT();

        // ACT
        sut.logout();

        // VERIFY
        verify(jwtTokenRepositoryMock, only()).save(any());

    }

    @Test
    void logout_missingJWT_throwsException() {

        // SETUP
        doReturn(null).when(sut).getCurrentJWT();

        // VERIFY
        final BadRequestException resultException = assertThrows(BadRequestException.class,
                () -> sut.logout());
        assertEquals(TOKEN + IS_MISSING, resultException.getMessage());
        verify(jwtTokenRepositoryMock, never()).save(any());

    }

    @Test
    public void getByAccessToken_clearCase_correctBehaviour() {

        // SETUP
        when(jwtTokenRepositoryMock.getByAccessToken(anyString())).thenReturn(jwtToken);

        // ACT
        sut.getByAccessToken(anyString());

        // VERIFY
        verify(jwtTokenRepositoryMock, only()).getByAccessToken(any());

    }

    @Test
    public void getByAccessToken_accessTokenIsMissing_correctBehaviour() {

        // VERIFY
        final BadRequestException resultException = assertThrows(BadRequestException.class,
                () -> sut.getByAccessToken(null));
        assertEquals(ACCESS + SPACE + TOKEN + IS_MISSING, resultException.getMessage());
        verify(jwtTokenRepositoryMock, never()).getByAccessToken(any());

    }

    @Test
    public void getCurrentJWT_clearCase_correctBehaviour() {

        // SETUP
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getCredentials()).thenReturn(jwtToken);

        // ACT
        final JwtToken uut = sut.getCurrentJWT();

        // VERIFY
        assertNotNull(uut);

    }

    @Test
    public void validateToken_invalidTokenSignature_throwsUnauthorizedException() {

        // VERIFY
        final UnauthorizedException resultException = assertThrows(UnauthorizedException.class,
                () -> sut.validateToken(null));
        assertEquals(TOKEN_IS_NOT_VALID, resultException.getMessage());

    }

    @Test
    public void validateToken_missingToken_throwsUnauthorizedException() {

        // SETUP
        when(jwtTokenBuilderMock.isValidJWT(any())).thenReturn(TRUE);

        // VERIFY
        final UnauthorizedException resultException = assertThrows(UnauthorizedException.class,
                () -> sut.validateToken(null));
        assertEquals(TOKEN_NOT_EXISTS, resultException.getMessage());

    }

    @Test
    public void validateToken_invalidToken_throwsUnauthorizedException() {

        // SETUP
        when(jwtTokenBuilderMock.isValidJWT(any())).thenReturn(TRUE);
        jwtToken.setValid(FALSE);

        // VERIFY
        final UnauthorizedException resultException = assertThrows(UnauthorizedException.class,
                () -> sut.validateToken(jwtToken));
        assertEquals(TOKEN_IS_NOT_VALID, resultException.getMessage());

    }

    @Test
    public void validateToken_deletedToken_throwsUnauthorizedException() {

        // SETUP
        when(jwtTokenBuilderMock.isValidJWT(any())).thenReturn(TRUE);
        jwtToken.setDeleted(TRUE);

        // VERIFY
        final UnauthorizedException resultException = assertThrows(UnauthorizedException.class,
                () -> sut.validateToken(jwtToken));
        assertEquals(TOKEN_IS_NOT_VALID, resultException.getMessage());

    }

    @Test
    public void validateToken_invalidAccessTokenExpirationDate_throwsUnauthorizedException() {

        // SETUP
        when(jwtTokenBuilderMock.isValidJWT(any())).thenReturn(TRUE);
        jwtToken.setAccessTokenExpirationDateTime(LocalDateTime.MIN);

        // VERIFY
        final UnauthorizedException resultException = assertThrows(UnauthorizedException.class,
                () -> sut.validateToken(jwtToken));
        assertEquals(TOKEN_HAD_EXPIRED, resultException.getMessage());

    }

    @Test
    public void validateToken_invalidRefreshTokenExpirationDate_throwsUnauthorizedException() {

        // SETUP
        when(jwtTokenBuilderMock.isValidJWT(any())).thenReturn(TRUE);
        jwtToken.setRefreshTokenExpirationDateTime(LocalDateTime.MIN).setAccessTokenExpirationDateTime(LocalDateTime.MAX);

        // VERIFY
        final UnauthorizedException resultException = assertThrows(UnauthorizedException.class,
                () -> sut.validateToken(jwtToken));
        assertEquals(TOKEN_HAD_EXPIRED, resultException.getMessage());

    }

    @Test
    public void validateToken_clearCase_correctBehavior() {

        // SETUP
        when(jwtTokenBuilderMock.isValidJWT(any())).thenReturn(TRUE);
        jwtToken.setRefreshTokenExpirationDateTime(LocalDateTime.MAX).setAccessTokenExpirationDateTime(LocalDateTime.MAX);

        // ACT
        sut.validateToken(jwtToken);

    }

    @Test
    public void refreshToken_clearCase_correctBehavior() {

        // SETUP
        final String refreshToken = jwtTokenBuilder.getRefreshToken(userCredential, REFRESH_TOKEN_URL);
        when(jwtTokenRepositoryMock.getByRefreshToken(anyString())).thenReturn(jwtToken);

        // ACT
        sut.refreshToken(BEARER_SPACE + refreshToken);

        // VERIFY
        verify(jwtTokenMapperMock, only()).toDTO(any());

    }

    @Test
    public void refreshToken_missingBearerRefreshToken_throwsBadRequestException() {

        // VERIFY
        final BadRequestException resultException = assertThrows(BadRequestException.class,
                () -> sut.refreshToken(null));
        assertEquals(REFRESH_TOKEN + IS_MISSING, resultException.getMessage());

    }

    @Test
    public void refreshToken_invalidToken_throwsBadRequestException() {

        // VERIFY
        final BadRequestException resultException = assertThrows(BadRequestException.class,
                () -> sut.refreshToken("invalid Token"));
        assertEquals(REFRESH_TOKEN + IS_MISSING, resultException.getMessage());

    }

    @Test
    public void refreshToken_errorInTryBlock_throwsBadRequestException() {

        // SETUP
        final String refreshToken = jwtTokenBuilder.getRefreshToken(userCredential, REFRESH_TOKEN_URL);

        // VERIFY
        final BadRequestException resultException = assertThrows(BadRequestException.class,
                () -> sut.refreshToken(BEARER_SPACE + refreshToken));
        assertEquals(COULD_NOT + GENERATE + SPACE + REFRESH_TOKEN + COLON + SPACE + NULL, resultException.getMessage());

    }
}
