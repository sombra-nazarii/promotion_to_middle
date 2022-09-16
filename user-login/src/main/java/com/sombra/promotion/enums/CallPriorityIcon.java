package com.sombra.promotion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum CallPriorityIcon {

    ARROW_UPWARD("ARROW_UPWARD"),
    ARROW_DOWNWARD("ARROW_DOWNWARD"),
    DO_DISTURB_ON("DO_DISTURB_ON"),
    REPORT("REPORT");

    private final String value;

    public static CallPriorityIcon getEnum(String value) {
        for (CallPriorityIcon priorityDirection : values()) {
            if (priorityDirection.getValue().equalsIgnoreCase(value)) {
                return priorityDirection;
            }
        }
        throw new IllegalArgumentException("Wrong value for Call Priority Icon Enum");
    }

    public static List<String> getAllStringValues() {
        return Arrays.stream(values())
                .map(CallPriorityIcon::getValue)
                .collect(Collectors.toList());
    }
}
