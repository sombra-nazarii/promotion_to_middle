package com.sombra.promotion.security;

import com.sombra.promotion.entity.JwtToken;
import com.sombra.promotion.entity.UserCredential;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthentication implements Authentication {

    private final UserCredential userCredential;
    private final JwtToken jwtToken;

    public JwtAuthentication(UserCredential userCredential, JwtToken jwtToken) {
        this.userCredential = userCredential;
        this.jwtToken = jwtToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userCredential.getAuthorities();
    }

    @Override
    public JwtToken getCredentials() {
        return jwtToken;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userCredential;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean b) {
    }

    @Override
    public String getName() {
        return userCredential.getEmail();
    }
}
