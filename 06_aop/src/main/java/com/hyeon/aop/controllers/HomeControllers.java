package com.hyeon.aop.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hyeon.aop.services.MyCalcService;

@Controller
public class HomeControllers {
    
    // MyCalcService 를 주입받는다
    @Autowired
    private MyCalcService myCalcService;

    @GetMapping("/")
    public String home(Model model) {
        int value1 = myCalcService.plus(100,200);
        int value2 = myCalcService.minus(100,200);

        model.addAttribute("value1", value1);
        model.addAttribute("value2", value2);

        return "index";
    }
}