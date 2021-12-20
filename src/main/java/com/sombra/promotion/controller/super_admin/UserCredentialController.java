package com.sombra.promotion.controller.super_admin;

import com.sombra.promotion.dto.user_credential.UserCredentialDTO;
import com.sombra.promotion.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/super_admin")
@RestController
public class UserCredentialController {

    private final UserCredentialService userCredentialService;

    @GetMapping
    public ResponseEntity<List<UserCredentialDTO>> getAll(){
        return ResponseEntity.ok(userCredentialService.getAll());
    }
}
