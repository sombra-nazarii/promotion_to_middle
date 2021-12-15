package com.sombra.promotion.service.impl;

import com.sombra.promotion.entity.UserCredential;
import com.sombra.promotion.exception.NotFoundException;
import com.sombra.promotion.exception.UnauthorizedException;
import com.sombra.promotion.repository.UserCredentialRepository;
import com.sombra.promotion.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.sombra.promotion.util.Constants.*;
import static java.lang.String.format;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Service
public class UserCredentialServiceImpl implements UserCredentialService, UserDetailsService {

    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    @Override
    public void validateUserCredential(final UserCredential userCredential) {
        if (isNull(userCredential)) {
            throw new UnauthorizedException(NOT_AUTHORIZED);
        }
        if (!userCredential.isEnabled()) {
            throw new UnauthorizedException(String.format(USER + WITH_EMAIL + WAS + DISABLED, userCredential.getEmail()));
        }
        if (userCredential.isDeleted()) {
            throw new UnauthorizedException(NOT_AUTHORIZED);
        }
    }

    @Override
    public UserCredential getExistingByEmail(final String email) {
        return userCredentialRepository.getByEmail(email)
                .orElseThrow(() -> new NotFoundException(format(USER_CREDENTIAL + WITH_EMAIL + NOT_EXIST, email)));
    }

    @Override
    public void validatePassword(final String password, final String storedPassword) {
        if (!bCryptPasswordEncoder.matches(password, storedPassword)) {
            throw new UnauthorizedException(PASSWORD + NOT_VALID);
        }
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return getExistingByEmail(email);
    }
}
