package com.sombra.promotion.service;

import com.sombra.promotion.entity.User;

public interface UserService {

    User getExistingByUserCredentialId(Long userCredentialId);

}
