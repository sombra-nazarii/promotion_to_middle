package com.sombra.promotion.service.impl;

import com.sombra.promotion.entity.Role;
import com.sombra.promotion.enums.RoleEnum;
import com.sombra.promotion.exception.NotFoundException;
import com.sombra.promotion.repository.RoleRepository;
import com.sombra.promotion.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.sombra.promotion.util.Constants.*;
import static java.lang.String.format;

@AllArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getExistingByRoleName(RoleEnum roleName) {
        return roleRepository.getByName(roleName)
                .orElseThrow(() -> new NotFoundException(format(ROLE + WITH_NAME + NOT_EXIST, roleName.name())));
    }

    @Override
    public List<Role> getRoles(final Collection<String> roles) {
        return roles.stream()
                .map(RoleEnum::valueOf)
                .map(this::getExistingByRoleName)
                .collect(Collectors.toList());
    }
}
