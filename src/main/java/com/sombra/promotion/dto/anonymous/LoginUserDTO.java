package com.sombra.promotion.dto.anonymous;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.sombra.promotion.util.Constants.*;

@Data
@NoArgsConstructor
public class LoginUserDTO {

    @Pattern(regexp = EMAIL_REGEX, message = EMAIL + NOT_VALID)
    @NotBlank(message = EMAIL + NOT_EMPTY)
    private String email;
    @NotBlank(message = PASSWORD + NOT_EMPTY)
    private String password;

}
