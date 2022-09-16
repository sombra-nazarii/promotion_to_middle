package com.sombra.promotion.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static com.common.components.util.Constants.*;

@Data
@NoArgsConstructor
public class ServiceCallPriorityLevelDTO {

    @NotNull(message = ID + NOT_EMPTY)
    private Long id;

    @Min(value = 0, message = PRIORITY + LEVEL + MIN_VALIDATION_FORMATTED_MESSAGE)
    @Max(value = 6, message = PRIORITY + LEVEL + MAX_VALIDATION_FORMATTED_MESSAGE)
    @NotNull(message = PRIORITY + LEVEL + NOT_EMPTY)
    private Integer priorityLevel;

    public static ServiceCallPriorityLevelDTO createInstance(Long id, int priorityLevel) {
        return new ServiceCallPriorityLevelDTO()
                .setId(id)
                .setPriorityLevel(priorityLevel);
    }
}
