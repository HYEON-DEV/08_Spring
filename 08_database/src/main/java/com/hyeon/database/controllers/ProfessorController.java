package com.hyeon.database.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyeon.database.exceptions.ServiceNoResultException;
import com.hyeon.database.helpers.WebHelper;
import com.hyeon.database.models.Professor;
import com.hyeon.database.services.ProfessorService;

import jakarta.servlet.http.HttpServletRequest;

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

    /** 상세 조회 페이지
     * @param model 모델
     * @param profno 교수 번호
     * @return 교수 상세 화면을 구현한 View 경로
     */
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


    /** 교수 등록 화면
     * @return 교수 등록 화면 구현한 View 경로
     */
    @GetMapping("/professor/add")
    public String add() {
        return "/professor/add";
    }


    /**
     * 교수 등록 처리
     * Action 페이지들은 View를 사용하지 않고 다른 페이지로 이동해야 하므로 메서드 상단에 @ResponseBody를 적용하여 View 없이 직접 응답을 구현한다
     */
    @ResponseBody  // View를 사용하지 않음 (action페이지에 꼭 적용)
    @PostMapping("/professor/add_ok")
    public void addOk( HttpServletRequest request,
        @RequestParam("name") String name,
        @RequestParam("user_id") String userid,
        @RequestParam("position") String position,
        @RequestParam("sal") int sal,
        @RequestParam("hiredate") String hiredate,
        @RequestParam(value="comm", required=false) Integer comm,
        @RequestParam("deptno") int deptno) {

        String referer = request.getHeader("referer");

        if (referer==null || !referer.contains("/professor")) {
            webHelper.badRequest("올바르지 않은 접근입니다");
            return;
        }
        
        Professor professor = new Professor();
        professor.setName(name);
        professor.setUserid(userid);
        professor.setPosition(position);
        professor.setSal(sal);
        professor.setHiredate(hiredate);
        professor.setComm(comm);
        professor.setDeptno(deptno);

        try {
            professorService.addItem(professor);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        webHelper.redirect("/professor/detail/" + professor.getProfno(), "등록되었습니다");
    }


    /**
     * 교수 삭제 처리
     * @param request
     * @param profno 교수번호
     */
    @ResponseBody
    @GetMapping("/professor/delete/{profno}")
    public void delete( HttpServletRequest request,
        @PathVariable("profno") int profno) {
        
        String referer = request.getHeader("referer");

        if (referer==null || !referer.contains("/professor")) {
            webHelper.badRequest("올바르지 않은 접근입니다");
            return;
        }

        Professor professor = new Professor();
        professor.setProfno(profno);

        try {
            professorService.deleteItem(professor);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        webHelper.redirect("/professor", "삭제되었습니다");        
    }


    /**
     * 교수 수정 페이지
     * @param model - Model 객체
     * @param profno - 교수 번호
     * @return View 페이지 경로
     */
    @GetMapping("/professor/edit/{profno}")
    public String edit( Model model, @PathVariable("profno") int profno ) {

        Professor params = new Professor();
        params.setProfno(profno);

        Professor professor = null;

        try {
            professor = professorService.getItem(params);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        model.addAttribute("professor", professor);

        return "/professor/edit";
    }


    @ResponseBody
    @PostMapping("/professor/edit_ok/{profno}")
    public void edit_ok(
        @PathVariable("profno") int profno,
        @RequestParam("name") String name,
        @RequestParam("user_id") String userid,
        @RequestParam("position") String position,
        @RequestParam("sal") int sal,
        @RequestParam("hiredate") String hiredate,
        @RequestParam(value="comm", required=false) Integer comm,
        @RequestParam("deptno") int deptno) {
        
        // 수정에 사용될 값을 beans에 담는다
        Professor professor = new Professor();
        professor.setProfno(profno);
        professor.setName(name);
        professor.setUserid(userid);
        professor.setPosition(position);
        professor.setSal(sal);
        professor.setHiredate(hiredate);
        professor.setDeptno(deptno);

        if (comm != null) {
            professor.setComm(comm);
        } else {
            professor.setComm(null);
        }

        try {
            professor = professorService.editItem(professor);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // 수정결과 확인을 위해 상세페이지로 이동
        webHelper.redirect("/professor/detail/" + professor.getProfno(), "수정되었습니다");
    }



}
