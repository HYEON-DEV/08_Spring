package com.hyeon.params.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hyeon.params.helpers.RegexHelper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class PostController {
    
    @GetMapping("/post/home")
    public String home() {
        return "post/home";
    }

    /** POST 방식의 파라미터를 전송받기 위한 컨트롤러 메서드 */
    @PostMapping("/post/answer")
    public String post(Model model, HttpServletResponse response, @RequestParam("user_name") String name, @RequestParam("user_age") String age) {
        // POST 파라미터 받기 => 
        // 값이 입력되지 않았을 경우를 대비해 문자열로 선언해야 한다

        RegexHelper regex = RegexHelper.getInstance();
        
        try {
            regex.isValue(name, "이름을 입력하세요");
            regex.isKor(name, "이름은 한글만 가능합니다");
            regex.isValue(age, "나이를 입력하세요");
            regex.isNum(age, "나이는 숫자만 가능합니다");
        } catch (Exception e) {
            log.error("입력값 유효성 검사 실패", e);
            // alert메시지 표시 후 이전 페이지로 이동하는 처리
            response.setContentType("text/html; charset=utf-8");

            try {
                PrintWriter out = response.getWriter();
                out.println("<script>");
                out.println("alert('" + e.getMessage() + "')");
                out.println("history.back()");
                out.println("</script>");
                out.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            return null;
        }

        // 파라미터값을 View에 전달
        model.addAttribute("name", name);
        model.addAttribute("ageGroup", Integer.parseInt(age)/10 * 10);
            

        return "post/result";
    }
}
