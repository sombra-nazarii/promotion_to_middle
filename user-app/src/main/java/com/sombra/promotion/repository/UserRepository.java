package com.sombra.promotion.repository;

import com.sombra.promotion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserCredentialId(@Param(value = "userCredentialId") Long userCredentialId);

}
