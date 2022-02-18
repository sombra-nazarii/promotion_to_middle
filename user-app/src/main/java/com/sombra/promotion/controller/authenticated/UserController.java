package com.sombra.promotion.controller.authenticated;

import com.sombra.promotion.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@AllArgsConstructor
@RequestMapping("/api/user")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/{id}")
    ResponseEntity<HttpStatus> createUse(@PathVariable("id") Long id,
                                         @RequestBody Collection<String> roles) {
        userService.createUser(id, roles);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{id}")
    ResponseEntity<HttpStatus> updateUserRole(@PathVariable("id") Long id,
                                              @RequestBody Collection<String> roles) {
        userService.updateUserRole(id, roles);
        return ResponseEntity.noContent().build();
    }
}
