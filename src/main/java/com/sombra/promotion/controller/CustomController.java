package com.sombra.promotion.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@AllArgsConstructor
public class CustomController {

    @GetMapping("/admin")
    public String admin(){
        return "ADMIN";
    }

    @GetMapping("/user")
    public String user(){
        return "USER";
    }
}
