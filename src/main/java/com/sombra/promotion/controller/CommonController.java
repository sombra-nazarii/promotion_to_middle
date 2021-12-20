package com.sombra.promotion.controller;

import com.sombra.promotion.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/public")
@RestController
public class CommonController {

    private final JwtTokenService jwtTokenService;

    @GetMapping("/verify/token")
    public ResponseEntity<Boolean> verifyToken(@RequestBody String accessToken){
        return ResponseEntity.ok(jwtTokenService.verifyAccessToken(accessToken));
    }
}
