package com.sombra.promotion.service;

import com.sombra.promotion.entity.Role;
import com.sombra.promotion.enums.RoleEnum;

public interface RoleService {

    Role getExistingByRoleName(RoleEnum roleName);

}
