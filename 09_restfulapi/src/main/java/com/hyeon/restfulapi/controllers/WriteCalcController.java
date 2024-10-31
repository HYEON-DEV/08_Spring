package com.hyeon.restfulapi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WriteCalcController {
    
    @GetMapping("/calc")
    public String calc() {
      
        return "calc.html";
    }
}
