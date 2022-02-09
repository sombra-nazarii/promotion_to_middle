package com.sombra.promotion.service;

import com.sombra.promotion.entity.User;

import java.util.Collection;

public interface UserService {

    User getExistingUserByUserCredentialId(Long userCredentialId);

    void updateUserRole(Long userCredentialId, Collection<String> roles);
    
}
