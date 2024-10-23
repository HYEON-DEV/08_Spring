package com.hyeon.database.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hyeon.database.exceptions.ServiceNoResultException;
import com.hyeon.database.helpers.WebHelper;
import com.hyeon.database.models.Professor;
import com.hyeon.database.services.ProfessorService;

@Controller
public class ProfessorController {
    
    @Autowired
    private ProfessorService professorService;

    @Autowired
    private WebHelper webHelper;


    /**
     * 교수 목록 화면
     * @param model - 모델
     * @return 교수 목록 화면을 구현할 View 경로
     */
    @GetMapping("/professor")
    public String index(Model model) {
        List<Professor> professors = null;

        try {
            professors = professorService.getList(null);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
            return null;
        } catch (Exception e) {
            webHelper.serverError(e);
            return null;
        }

        model.addAttribute("professors", professors);

        return "/professor/index";
    }


    @GetMapping("/professor/detail/{profno}")
    public String detail(Model model, @PathVariable("profno") int profno) {

        // 조회 조건에 사용할 변수를 Beans에 저장
        Professor params = new Professor();
        params.setProfno(profno);

        // 조회 결과를 저장할 객체 선언
        Professor professor = null;

        try {
            // 데이터 조회
            professor = professorService.getItem(params);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // View에 데이터 전달
        model.addAttribute("professor", professor);

        return "/professor/detail";
    }
}