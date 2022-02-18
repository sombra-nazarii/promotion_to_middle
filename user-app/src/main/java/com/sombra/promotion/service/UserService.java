package com.sombra.promotion.service;

import com.sombra.promotion.entity.User;

import java.util.Collection;

public interface UserService {

    User getExistingUserByUserCredentialId(Long userCredentialId);

    boolean isUserExistByUserCredentialId(Long userCredentialId);

    void updateUserRole(Long userCredentialId, Collection<String> roles);

    void createUser(Long userCredentialId, Collection<String> roles);

}
