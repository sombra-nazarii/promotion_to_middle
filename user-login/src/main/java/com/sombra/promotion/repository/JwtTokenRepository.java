package com.sombra.promotion.repository;

import com.sombra.promotion.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    JwtToken getByAccessToken(@Param("accessToken") String accessToken);

    JwtToken getByRefreshToken(@Param("refreshToken") String refreshToken);

    @Query(value = "SELECT t from JwtToken t WHERE t.userCredential.email LIKE :email")
    Optional<JwtToken> getByUserCredentialEmail(@Param("email") String email);
}
