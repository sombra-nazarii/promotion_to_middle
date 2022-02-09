package com.sombra.promotion.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/public")
@RestController
public class CommonController {

    @GetMapping(value = "/health-check")
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.ok("Hey!");
    }
}
