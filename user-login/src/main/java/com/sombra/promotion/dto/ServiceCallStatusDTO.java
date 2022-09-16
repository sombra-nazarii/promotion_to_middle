package com.sombra.promotion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static com.common.components.util.Constants.*;

@Data
@NoArgsConstructor
public class ServiceCallStatusDTO {

    private Long id;
    @NotNull(message = STATUS_UP + SPACE + NAME + NOT_EMPTY)
    private String name;
    @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})")
    private String statusColor;
    @NotNull(message = DEFAULT + NOT_EMPTY)
    private Boolean isDefault;
    @NotNull(message = DELETED + SPACE + COLOR + NOT_EMPTY)
    private Boolean deleted;
    private Boolean canBeDeleted;

    public static ServiceCallStatusDTO createInstance(Long id, String name, String statusColor,
                                                      boolean isDefault, boolean deleted, boolean canBeDeleted) {
        return new ServiceCallStatusDTO()
                .setId(id)
                .setName(name)
                .setStatusColor(statusColor)
                .setIsDefault(isDefault)
                .setDeleted(deleted)
                .setCanBeDeleted(canBeDeleted);
    }
}
