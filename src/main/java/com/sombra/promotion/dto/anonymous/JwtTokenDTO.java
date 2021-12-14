package com.sombra.promotion.dto.anonymous;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtTokenDTO {

    private String accessToken;
    private String refreshToken;

    public static JwtTokenDTO createInstance(final String accessToken, final String refreshToken) {
        return new JwtTokenDTO()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
    }
}
