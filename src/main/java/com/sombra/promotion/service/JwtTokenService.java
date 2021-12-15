package com.sombra.promotion.service;

import com.sombra.promotion.dto.anonymous.JwtTokenDTO;
import com.sombra.promotion.dto.anonymous.LoginUserDTO;
import com.sombra.promotion.entity.JwtToken;

public interface JwtTokenService {

    JwtTokenDTO login(LoginUserDTO loginUserDTO);

    void validateToken(final JwtToken jwtToken);

    JwtToken getByAccessToken(String token);

    JwtTokenDTO refreshToken(String refreshToken);

    void logout();

    JwtToken getCurrentJWT();

}
