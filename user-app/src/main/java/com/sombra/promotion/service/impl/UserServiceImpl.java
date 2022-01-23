package com.sombra.promotion.service.impl;

import com.sombra.promotion.entity.User;
import com.sombra.promotion.exception.NotFoundException;
import com.sombra.promotion.repository.UserRepository;
import com.sombra.promotion.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.sombra.promotion.util.Constants.*;
import static java.lang.String.format;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public User getExistingByUserCredentialId(final Long userCredentialId) {
        return userRepository.findUserByUserCredentialId(userCredentialId)
                .orElseThrow(() -> new NotFoundException(format(USER + WITH_ID + NOT_EXIST, userCredentialId)));
    }

    @Override
    public UserDetails loadUserByUsername(String userCredentialId) throws UsernameNotFoundException {
        return getExistingByUserCredentialId(Long.valueOf(userCredentialId));
    }
}
