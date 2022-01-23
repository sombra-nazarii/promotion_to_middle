package com.sombra.promotion.enums;

import com.sombra.promotion.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Getter
@AllArgsConstructor
public enum RoleEnum implements GrantedAuthority {

    ROLE_SUPER_ADMIN(Constants.Roles.ROLE_SUPER_ADMIN),
    ROLE_USER(Constants.Roles.ROLE_USER);

    private final String paramName;

    public static List<String> getAllStringValues() {
        return stream(values())
                .map(RoleEnum::getParamName)
                .collect(Collectors.toList());
    }

    @Override
    public String getAuthority() {
        return paramName;
    }
}
