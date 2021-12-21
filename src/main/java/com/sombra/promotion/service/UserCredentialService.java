package com.sombra.promotion.service;

import com.sombra.promotion.dto.user_credential.UserCredentialCreateDTO;
import com.sombra.promotion.dto.user_credential.UserCredentialDTO;
import com.sombra.promotion.entity.UserCredential;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserCredentialService extends UserDetailsService {

    void validateUserCredential(UserCredential userCredential);

    UserCredential getExistingByEmail(String email);

    void validatePassword(String password, String storedPassword);

    List<UserCredentialDTO> getAll();

    UserCredentialDTO createUserCredential(UserCredentialCreateDTO userCredential);

}
