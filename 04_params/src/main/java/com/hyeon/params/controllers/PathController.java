package com.hyeon.params.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PathController {
    
    @GetMapping("/path/home")
    public String home() {
        return "path/home";
    }

    /** PATH 파라미터를 전송받기 위한 컨트롤러 메서드 */
    @GetMapping("/mypath/{txt1}/{txt2}/{num}")
    public String mypath(Model model, @PathVariable("txt1") String txt1, @PathVariable("txt2") String txt2, @PathVariable("num") int num) {

        // 파라미터 값 view에 전달
        model.addAttribute("txt1", txt1);
        model.addAttribute("txt2", txt2);
        model.addAttribute("num", num);

        return "path/mypath";
    }
}
