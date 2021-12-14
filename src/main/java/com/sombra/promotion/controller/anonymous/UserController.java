package com.sombra.promotion.controller.anonymous;

import com.sombra.promotion.dto.anonymous.JwtTokenDTO;
import com.sombra.promotion.dto.anonymous.LoginUserDTO;
import com.sombra.promotion.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
@RequestMapping("/api/public")
@RestController
public class UserController {

    private final JwtTokenService jwtTokenService;

    @PostMapping(value = "/login")
    public ResponseEntity<JwtTokenDTO> login(@RequestBody @Valid final LoginUserDTO loginUserDTO) {
        return ok(jwtTokenService.login(loginUserDTO));
    }

    @PutMapping("/token/refresh")
    public ResponseEntity<JwtTokenDTO> refreshToken(@RequestBody String refreshToken) {
        return ResponseEntity.ok(jwtTokenService.refreshToken(refreshToken));
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<Void> logout() {
        jwtTokenService.logout();
        return ResponseEntity.ok().build();
    }
}
