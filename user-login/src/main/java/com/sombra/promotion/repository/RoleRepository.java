package com.sombra.promotion.repository;

import com.sombra.promotion.entity.Role;
import com.sombra.promotion.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> getByName(@Param("roleName") RoleEnum roleName);

}
