package com.sombra.promotion.mapper;

import com.sombra.promotion.dto.anonymous.JwtTokenDTO;
import com.sombra.promotion.entity.JwtToken;
import org.springframework.stereotype.Component;

import static com.sombra.promotion.util.Constants.BEARER_SPACE;

@Component
public class JwtTokenMapper {

    public JwtTokenDTO toDTO(JwtToken jwtToken) {
        return JwtTokenDTO.createInstance(BEARER_SPACE + jwtToken.getAccessToken(), jwtToken.getRefreshToken());
    }
}
