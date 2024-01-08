package com.ensias.eldycare.authenticationservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/filter-test")
public class FilterTestController {
    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
