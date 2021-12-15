package com.sombra.promotion.service.impl;

import com.sombra.promotion.entity.UserCredential;
import com.sombra.promotion.exception.NotFoundException;
import com.sombra.promotion.exception.UnauthorizedException;
import com.sombra.promotion.repository.UserCredentialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.sombra.promotion.util.Constants.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserCredentialServiceImplTest {

    @Mock
    private UserCredentialRepository userCredentialRepositoryMock;
    @Mock
    private PasswordEncoder bCryptPasswordEncoderMock;

    @Spy
    @InjectMocks
    private UserCredentialServiceImpl sut;

    private UserCredential userCredential;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
        userCredential = new UserCredential()
                .setDeleted(FALSE)
                .setEnabled(TRUE)
                .setPassword("$2a$10$wIQYCdbRtEaJNsU71a2PTu3MzNqLdbJmQHcSCvAcOb33WLUUPOrLC");
    }

    @Test
    public void validateUserCredential_clearCase_correctBehaviour() {

        // ACT
        sut.validateUserCredential(userCredential);

    }

    @Test
    public void validateUserCredential_missingUserCredential_throwsUnauthorizedException() {

        // VERIFY
        final UnauthorizedException resultException = assertThrows(UnauthorizedException.class,
                () -> sut.validateUserCredential(null));
        assertEquals(NOT_AUTHORIZED, resultException.getMessage());

    }

    @Test
    public void validateUserCredential_disabledUserCredential_throwsUnauthorizedException() {

        // SETUP
        userCredential.setEnabled(FALSE);

        // VERIFY
        final UnauthorizedException resultException = assertThrows(UnauthorizedException.class,
                () -> sut.validateUserCredential(userCredential));
        assertEquals(String.format(USER + WITH_EMAIL + WAS + DISABLED, userCredential.getEmail()), resultException.getMessage());

    }

    @Test
    public void validateUserCredential_deletedUserCredential_throwsUnauthorizedException() {

        // SETUP
        userCredential.setDeleted(TRUE);

        // VERIFY
        final UnauthorizedException resultException = assertThrows(UnauthorizedException.class,
                () -> sut.validateUserCredential(userCredential));
        assertEquals(NOT_AUTHORIZED, resultException.getMessage());

    }

    @Test
    public void getExistingByEmail_clearCase_correctBehaviour() {

        // SETUP
        when(userCredentialRepositoryMock.getByEmail(anyString())).thenReturn(Optional.of(userCredential));

        // ACT
        final UserCredential uut = sut.getExistingByEmail("email");

        // VERIFY
        assertNotNull(uut);
        verify(userCredentialRepositoryMock, only()).getByEmail(anyString());

    }

    @Test
    public void getExistingByEmail_missingUserCredential_correctBehaviour() {

        // SETUP
        when(userCredentialRepositoryMock.getByEmail(anyString())).thenReturn(null);

        // VERIFY
        final NotFoundException resultException = assertThrows(NotFoundException.class,
                () -> sut.getExistingByEmail(null));
        assertEquals(format(USER_CREDENTIAL + WITH_EMAIL + NOT_EXIST, null), resultException.getMessage());

    }

    @Test
    public void loadUserByUsername_clearCase_correctBehaviour() {

        // SETUP
        when(userCredentialRepositoryMock.getByEmail(anyString())).thenReturn(Optional.of(userCredential));
        doReturn(userCredential).when(sut).getExistingByEmail(anyString());

        // ACT
        final UserDetails uut = sut.loadUserByUsername("email");

        // VERIFY
        assertNotNull(uut);

    }

    @Test
    public void validatePassword_clearCase_correctBehaviour() {

        // SETUP
        when(bCryptPasswordEncoderMock.matches(anyString(), anyString())).thenReturn(TRUE);

        // ACT
        sut.validatePassword(userCredential.getPassword(), "kodpizda1234");

    }

    @Test
    public void validatePassword_differentPasswords_correctBehaviour() {

        // SETUP
        when(bCryptPasswordEncoderMock.matches(anyString(), anyString())).thenReturn(FALSE);

        // VERIFY
        final UnauthorizedException resultException = assertThrows(UnauthorizedException.class,
                () -> sut.validatePassword(userCredential.getPassword(), "kodpizda1234"));
        assertEquals(PASSWORD + NOT_VALID, resultException.getMessage());

    }
}