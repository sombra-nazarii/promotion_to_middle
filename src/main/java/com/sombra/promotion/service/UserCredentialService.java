package com.sombra.promotion.service;

import com.sombra.promotion.entity.JwtToken;
import com.sombra.promotion.entity.UserCredential;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserCredentialService extends UserDetailsService {

    UserCredential getExistingByToken(JwtToken jwtToken);

    void validateUserCredential(UserCredential userCredential);

    UserCredential getExistingByEmail(String email);

    void validatePassword(String password, String storedPassword);

}
