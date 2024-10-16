package com.hyeon.cookie_session.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionController {
    /** 세션 저장을 위한 작성 페이지 */
    @GetMapping("/session/home")
    public String home(Model model) {
        return "/session/home";
    }

    /** 심플 로그인 홈 */
    @GetMapping("/session/login")
    public String login() {
        return "/session/login";
    }    
}
