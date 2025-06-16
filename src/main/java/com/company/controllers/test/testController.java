package com.company.controllers.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {

    @GetMapping("/test")
    public String testEndPoint(){
        return "This is test endpoint";
    }

}
