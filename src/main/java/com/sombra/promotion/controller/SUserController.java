package com.sombra.promotion.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class SUserController {

    @GetMapping("/bla")
    public ResponseEntity<String> getBla() {
        return ResponseEntity.ok("bla");
    }

    @GetMapping("/foo")
    public ResponseEntity<String> getFoo() {
        return ResponseEntity.ok("foo");
    }
}
