package com.sombra.promotion.service.impl;

import com.sombra.promotion.dto.user_credential.UserCredentialCreateDTO;
import com.sombra.promotion.dto.user_credential.UserCredentialDTO;
import com.sombra.promotion.dto.user_credential.UserCredentialUpdateDTO;
import com.sombra.promotion.entity.Role;
import com.sombra.promotion.entity.UserCredential;
import com.sombra.promotion.enums.RoleEnum;
import com.sombra.promotion.exception.InternalServerException;
import com.sombra.promotion.exception.NotFoundException;
import com.sombra.promotion.exception.UnauthorizedException;
import com.sombra.promotion.mapper.UserCredentialMapper;
import com.sombra.promotion.repository.UserCredentialRepository;
import com.sombra.promotion.service.RoleService;
import com.sombra.promotion.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import static java.lang.Boolean.TRUE;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserCredentialServiceImpl implements UserCredentialService, UserDetailsService {

    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final UserCredentialMapper userCredentialMapper;
    private final RoleService roleService;
    private final RestTemplateService restTemplateService;

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
    public UserCredential getExistingById(final Long id) {
        return userCredentialRepository.findByIdNSD(id)
                .orElseThrow(() -> new NotFoundException(format(USER_CREDENTIAL + WITH_ID + NOT_EXIST, id)));
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
    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
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
        log.info("Created new User Credential with email {}", userCredential.getEmail());
        return userCredentialMapper.toDTO(savedUserCredential);
    }

    @Override
    @Transactional
    public UserCredentialDTO updateUserCredential(final UserCredentialUpdateDTO userCredentialToUpdate) {
        final UserCredential userCredential = getExistingById(userCredentialToUpdate.getId());

        final List<Role> roles = userCredentialToUpdate.getRoles().stream()
                .map(RoleEnum::valueOf)
                .map(roleService::getExistingByRoleName)
                .collect(Collectors.toList());

        userCredential
                .setEmail(userCredentialToUpdate.getEmail())
                .setRoles(roles);

        if (restTemplateService.isUserAppServiceHealthy()) {
            restTemplateService.updateUserRoles(userCredential.getId(), userCredentialToUpdate.getRoles());
        } else {
            log.error("User App is Broken");
            throw new InternalServerException("Can't update user");
        }

        final UserCredential savedUserCredential = userCredentialRepository.save(userCredential);
        log.info("Updated User Credential with email {}", userCredential.getEmail());
        return userCredentialMapper.toDTO(savedUserCredential);
    }

    @Override
    @Transactional
    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    public void deleteUserCredential(final String email) {
        final UserCredential userCredential = getExistingByEmail(email);

        userCredential
                .setDeleted(TRUE)
                .setEnabled(FALSE);

        userCredentialRepository.save(userCredential);
        log.info("Deleted User Credential with email {}", userCredential.getEmail());
    }

    @Override
    @Transactional
    @PreAuthorize(value = "hasRole('ROLE_SUPER_ADMIN')")
    public void changeEnableStatusUserCredential(final Long id, final Boolean enableStatus) {
        final UserCredential userCredential = getExistingById(id);

        if (!userCredential.isDeleted() && nonNull(userCredential.getLastLogged())) {
            userCredential.setEnabled(enableStatus);

            userCredentialRepository.save(userCredential);
            log.info("Changed User Credential enabled status to {}", enableStatus);
        }
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        return getExistingByEmail(email);
    }

    public static UserCredential getCurrentUserCredential() {
        final Object userCredential = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        if (nonNull(userCredential) && userCredential instanceof UserCredential) {
            return (UserCredential) userCredential;
        } else {
            throw new InternalServerException("Can't get User Credential");
        }
    }
}
