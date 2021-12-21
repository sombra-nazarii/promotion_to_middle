package com.sombra.promotion.mapper;

import com.sombra.promotion.dto.user_credential.UserCredentialDTO;
import com.sombra.promotion.entity.UserCredential;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class UserCredentialMapper {

    public UserCredentialDTO toDTO(final UserCredential userCredential) {
        return UserCredentialDTO.createInstance(userCredential);
    }

    public List<UserCredentialDTO> toDTOList(final List<UserCredential> userCredentials) {
        if (isNull(userCredentials)) {
            return new ArrayList<>();
        }
        return userCredentials.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
