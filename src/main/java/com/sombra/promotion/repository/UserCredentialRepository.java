package com.sombra.promotion.repository;

import com.sombra.promotion.entity.JwtToken;
import com.sombra.promotion.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {

    @Query(value = "SELECT u FROM UserCredential u " +
            "WHERE u.email LIKE :email " +
            "AND u.deleted = FALSE ")
    Optional<UserCredential> getByEmail(@Param("email") String email);

    @Query(value = "SELECT u FROM UserCredential u " +
            "WHERE u.jwtToken = :jwtToken " +
            "AND u.deleted = FALSE ")
    Optional<UserCredential> getByJwtToken(@Param("jwtToken") JwtToken jwtToken);

}
