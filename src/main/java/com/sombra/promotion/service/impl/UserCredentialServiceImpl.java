package com.sombra.promotion.service.impl;

import com.sombra.promotion.dto.user_credential.UserCredentialCreateDTO;
import com.sombra.promotion.dto.user_credential.UserCredentialDTO;
import com.sombra.promotion.entity.Role;
import com.sombra.promotion.entity.UserCredential;
import com.sombra.promotion.enums.RoleEnum;
import com.sombra.promotion.exception.NotFoundException;
import com.sombra.promotion.exception.UnauthorizedException;
import com.sombra.promotion.mapper.UserCredentialMapper;
import com.sombra.promotion.repository.UserCredentialRepository;
import com.sombra.promotion.service.RoleService;
import com.sombra.promotion.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.sombra.promotion.util.Constants.*;
import static java.lang.Boolean.FALSE;
import static java.lang.String.format;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Service
public class UserCredentialServiceImpl implements UserCredentialService, UserDetailsService {

    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final UserCredentialMapper userCredentialMapper;
    private final RoleService roleService;

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
    @Transactional(readOnly = true)
    public List<UserCredentialDTO> getAll() {
        final List<UserCredential> userCredentials = userCredentialRepository.findAll();
        return userCredentialMapper.toDTOList(userCredentials);
    }

    @Override
    @Transactional
    public UserCredentialDTO createUserCredential(final UserCredentialCreateDTO userCredentialToCreate) {
        final List<Role> roles = userCredentialToCreate.getRoles().stream()
                .map(RoleEnum::valueOf)
                .map(roleService::getExistingByRoleName)
                .collect(Collectors.toList());

        final UserCredential userCredential = UserCredential.createInstance(null,
                userCredentialToCreate.getEmail(),
                null,
                roles,
                null,
                FALSE,
                FALSE);

        final UserCredential savedUserCredential = userCredentialRepository.save(userCredential);
        return userCredentialMapper.toDTO(savedUserCredential);
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return getExistingByEmail(email);
    }
}
