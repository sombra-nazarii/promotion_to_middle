package com.sombra.promotion.service;

import com.sombra.promotion.entity.UserCredential;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserCredentialService extends UserDetailsService {

    void validateUserCredential(UserCredential userCredential);

    UserCredential getExistingByEmail(String email);

    void validatePassword(String password, String storedPassword);

}
