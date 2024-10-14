package com.hyeon.interceptor.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class MyController {
    // 일반적으로 컨트롤러 메서드의 URL, 메서드 이름, View 이름을 동일하게 맞추는 것이 관례
    // 모든 컨트롤러 메서드가 같은 View 이름을 반환하도록 구성

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("where", "인덱스 페이지");
        return "myview";
    }

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("where", "hello 페이지");
        return "myview";
    }

    @GetMapping("/hi")
    public String hi(Model model) {
        model.addAttribute("where", "hi 페이지");
        return "myview";
    }

    @GetMapping("/bye")
    public String bye(Model model) {
        model.addAttribute("where", "bye 페이지");
        return "myview";
    }
}
