package com.sombra.promotion.dto.user_credential;

import com.sombra.promotion.entity.UserCredential;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserCredentialDTO {

    private Long id;
    private String email;
    private Collection<String> roles = new ArrayList<>();
    private LocalDateTime lastLogged;
    private boolean enabled;
    private boolean deleted;

    public static UserCredentialDTO createInstance(final UserCredential userCredential) {

        final List<String> roles = userCredential.getRoles().stream()
                .map(role -> role.getName().getAuthority())
                .collect(Collectors.toList());

        return new UserCredentialDTO()
                .setId(userCredential.getId())
                .setEmail(userCredential.getEmail())
                .setRoles(roles)
                .setLastLogged(userCredential.getLastLogged())
                .setEnabled(userCredential.isEnabled())
                .setDeleted(userCredential.isDeleted());
    }
}
