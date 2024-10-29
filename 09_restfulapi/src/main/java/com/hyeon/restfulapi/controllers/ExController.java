package com.hyeon.restfulapi.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ExController {
    
    @GetMapping("/my_calc")
    public Map<String, Object> plus( HttpServletResponse response,
    @RequestParam("x") int x, @RequestParam("y") int y ) {
        
        Map<String, Object> output = new LinkedHashMap<String, Object>();

        output.put("x", x);
        output.put("y", y);
        output.put("result", x+y);

        return output;
    }

    @PostMapping("/my_calc")
    public Map<String, Object> minus( HttpServletResponse response,
    @RequestParam("x") int x, @RequestParam("y") int y ) {
        
        Map<String, Object> output = new LinkedHashMap<String, Object>();

        output.put("x", x);
        output.put("y", y);
        output.put("result", x-y);

        return output;
    }

    @PutMapping("/my_calc")
    public Map<String, Object> mult( HttpServletResponse response,
    @RequestParam("x") int x, @RequestParam("y") int y ) {
        
        response.setContentType("application/json; charset=UTF-8");
        
        response.setStatus(200);

        Map<String, Object> output = new LinkedHashMap<String, Object>();

        output.put("x", x);
        output.put("y", y);
        output.put("result", x*y);

        return output;
    }

    @DeleteMapping("/my_calc")
    public Map<String, Object> divide( HttpServletResponse response,
    @RequestParam("x") int x, @RequestParam("y") int y ) {
        
        response.setContentType("application/json; charset=UTF-8");
        
        response.setStatus(200);

        Map<String, Object> output = new LinkedHashMap<String, Object>();

        output.put("x", x);
        output.put("y", y);
        if (y==0) {
            output.put("result","나눌 수 없습니다");
        } else {
            output.put("result", x/y);
        }

        return output;
    }

}
