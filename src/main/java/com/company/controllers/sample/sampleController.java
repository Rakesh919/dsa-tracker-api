package com.company.controllers.sample;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class sampleController {

    @GetMapping("/")
    public String sample(){
        return "Hello from Spring boot";
    }
}
