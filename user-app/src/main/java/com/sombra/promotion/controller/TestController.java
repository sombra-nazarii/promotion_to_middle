package com.sombra.promotion.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/public")
@RestController
public class TestController {

    public String sayHello(){
        return "Hello";
    }
}
