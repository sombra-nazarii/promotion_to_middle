package com.sombra.promotion.dto;

import com.orca.enums.CallPriorityIcon;
import com.orca.enums.RespondTimeType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

import static com.common.components.util.Constants.*;

@Data
@NoArgsConstructor
public class ServiceCallPriorityDTO {

    private Long id;

    @Min(value = 0, message = PRIORITY + LEVEL + MIN_VALIDATION_FORMATTED_MESSAGE)
    @Max(value = 6, message = PRIORITY + LEVEL + MAX_VALIDATION_FORMATTED_MESSAGE)
    @NotNull(message = PRIORITY + LEVEL + NOT_EMPTY)
    private Integer priorityLevel;

    @NotNull(message = STATUS_UP + SPACE + NAME + NOT_EMPTY)
    private String name;

    @Size(max = 40, message = DESC_U + MAX_VALIDATION_FORMATTED_MESSAGE)
    private String description;

    @NotNull(message = PRIORITY + COLOR + NOT_EMPTY)
    @Pattern(regexp = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})")
    private String priorityColor;

    @NotNull(message = PRIORITY + ICON + NOT_EMPTY)
    private CallPriorityIcon priorityIcon;

    @Min(value = 0, message = RESPONSE_HOURS + MIN_VALIDATION_FORMATTED_MESSAGE)
    @NotNull(message = RESPONSE_HOURS + NOT_EMPTY)
    private Integer respondHours;

    @NotNull(message = RESPONSE + TIME + TYPE + NOT_EMPTY)
    private RespondTimeType respondTimeType;

    @NotNull(message = DELETED + SPACE + COLOR + NOT_EMPTY)
    private Boolean deleted;

    public static ServiceCallPriorityDTO createInstance(Long id, int priorityLevel, String name, String description,
                                                        String priorityColor, CallPriorityIcon priorityDirection,
                                                        int respondHours, RespondTimeType respondTimeType, boolean deleted) {
        return new ServiceCallPriorityDTO()
                .setId(id)
                .setPriorityLevel(priorityLevel)
                .setName(name)
                .setDescription(description)
                .setPriorityColor(priorityColor)
                .setPriorityIcon(priorityDirection)
                .setRespondHours(respondHours)
                .setRespondTimeType(respondTimeType)
                .setDeleted(deleted);
    }
}
