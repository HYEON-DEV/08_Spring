package com.hyeon.params.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SurveyController {
    
    /** 설문 시작 - 이름 입력 */
    @GetMapping("/survey/index")
    public String index() {
        return "survey/index";
    }

    /** 나이 입력 */
    @PostMapping("/survey/step1")
    public String step1(Model model, @RequestParam("name") String name) {
        // 이전 페이지에서 입력받은 내용을 view에 전달
        model.addAttribute("name", name);
        return "survey/step1";
    }

    /** 학생/교사 여부 선택 */
    @PostMapping("/survey/step2")
    public String step2(Model model, @RequestParam("name") String name, @RequestParam("age") int age) {
        // 이전 페이지에서 입력받은 내용을 view에 전달
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        return "survey/step2";
    }
    
    /** 안경 착용 여부 */
    @GetMapping("/survey/step3")
    public String step3(Model model, @RequestParam("name") String name, @RequestParam("age") int age, @RequestParam("group") String group) {
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        model.addAttribute("group", group);

        return "survey/step3";
    }

    /** 최종 결과 페이지 */
    @GetMapping("/survey/step4")
    public String step4(Model model, @RequestParam("name") String name, @RequestParam("age") int age, @RequestParam("group") String group, @RequestParam("glasses") String glasses) {
        model.addAttribute("name", name);
        model.addAttribute("age", age);
        model.addAttribute("group", group);
        model.addAttribute("glasses", glasses);

        return "survey/step4";
    }
    
}
