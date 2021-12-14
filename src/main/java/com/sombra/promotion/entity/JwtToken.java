package com.sombra.promotion.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.time.ZoneOffset.UTC;

@Data
@ToString(exclude = {"id", "userCredential"})
@EqualsAndHashCode(exclude = {"id", "userCredential"})
@NoArgsConstructor
@Entity
@Table(name = "jwt_token")
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "creation_date")
    private LocalDateTime creationDateTime;

    @Column(name = "access_token_expiration_date")
    private LocalDateTime accessTokenExpirationDateTime;

    @Column(name = "refresh_token_expiration_date")
    private LocalDateTime refreshTokenExpirationDateTime;

    @Column(name = "valid")
    private boolean valid;

    @Column(name = "deleted")
    private boolean deleted;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;

    @Column(name = "access_token")
    private String accessToken;

    @Column(name = "refresh_token")
    private String refreshToken;

    public static JwtToken createInstance(final UserCredential userCredential,
                                          final String accessToken,
                                          final String refreshToken,
                                          final long accessTokenExpirationSeconds,
                                          final long refreshTokenExpirationSeconds) {
        return new JwtToken()
                .setUserCredential(userCredential)
                .setAccessToken(accessToken)
                .setCreationDateTime(LocalDateTime.now(UTC))
                .setAccessTokenExpirationDateTime(LocalDateTime.now(UTC).plusSeconds(accessTokenExpirationSeconds))
                .setRefreshTokenExpirationDateTime(LocalDateTime.now(UTC).plusSeconds(refreshTokenExpirationSeconds))
                .setRefreshToken(refreshToken)
                .setValid(TRUE)
                .setDeleted(FALSE);
    }
}

