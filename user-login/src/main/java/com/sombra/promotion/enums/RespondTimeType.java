package com.sombra.promotion.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum RespondTimeType {

    HOUR("HOUR"),
    DAY("DAY"),
    WEEK("WEEK"),
    UNSPECIFIED("UNSPECIFIED");

    private final String value;

    public static RespondTimeType getEnum(String value) {
        for (RespondTimeType timeType : values()) {
            if (timeType.getValue().equalsIgnoreCase(value)) {
                return timeType;
            }
        }
        throw new IllegalArgumentException("Wrong value for Respond Time Type Enum");
    }

    public static List<String> getAllStringValues() {
        return Arrays.stream(values())
                .map(RespondTimeType::getValue)
                .collect(Collectors.toList());
    }
}
