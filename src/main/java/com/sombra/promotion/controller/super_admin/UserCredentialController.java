package com.sombra.promotion.controller.super_admin;

import com.sombra.promotion.dto.user_credential.UserCredentialCreateDTO;
import com.sombra.promotion.dto.user_credential.UserCredentialDTO;
import com.sombra.promotion.service.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/super_admin")
@RestController
public class UserCredentialController {

    private final UserCredentialService userCredentialService;

    @GetMapping("/")
    public ResponseEntity<List<UserCredentialDTO>> getAll(){
        return ResponseEntity.ok(userCredentialService.getAll());
    }

    @PostMapping("/")
    public ResponseEntity<UserCredentialDTO> createUserCredential(@RequestBody UserCredentialCreateDTO userCredential){
        return ResponseEntity.ok(userCredentialService.createUserCredential(userCredential));
    }
}
