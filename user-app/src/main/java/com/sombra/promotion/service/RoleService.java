package com.sombra.promotion.service;

import com.sombra.promotion.entity.Role;
import com.sombra.promotion.enums.RoleEnum;

import java.util.Collection;
import java.util.List;

public interface RoleService {

    Role getExistingByRoleName(RoleEnum roleName);

    List<Role> getRoles(Collection<String> roles);

}
