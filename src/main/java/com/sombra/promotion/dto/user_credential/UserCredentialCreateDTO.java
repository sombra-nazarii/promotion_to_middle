package com.sombra.promotion.dto.user_credential;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;

import static com.sombra.promotion.util.Constants.*;

@Data
public class UserCredentialCreateDTO {

    @Pattern(regexp = EMAIL_REGEX, message = EMAIL + NOT_VALID)
    @NotBlank(message = EMAIL + NOT_EMPTY)
    private String email;
    @NotEmpty(message = EMAIL + NOT_EMPTY)
    private Collection<String> roles = new ArrayList<>();

}
