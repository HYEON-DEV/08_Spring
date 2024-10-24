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
import com.hyeon.database.models.Student;
import com.hyeon.database.services.StudentService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class StudentController {
    
    @Autowired
    private StudentService studentService;

    @Autowired
    private WebHelper webHelper;


    /**
     * 학생 목록 화면
     * @param model - 모델
     * @return 학생 목록 화면을 구현할 View 경로
     */
    @GetMapping("/student")
    public String index(Model model) {
        List<Student> students = null;

        try {
            students = studentService.getList(null);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
            return null;
        } catch (Exception e) {
            webHelper.serverError(e);
            return null;
        }

        model.addAttribute("students", students);

        return "/student/index";
    }


    @GetMapping("/student/detail/{studno}")
    public String detail(Model model, @PathVariable("studno") int studno) {

        // 조회 조건에 사용할 변수를 Beans에 저장
        Student params = new Student();
        params.setStudno(studno);

        // 조회 결과를 저장할 객체 선언
        Student student = null;

        try {
            // 데이터 조회
            student = studentService.getItem(params);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // View에 데이터 전달
        model.addAttribute("student", student);

        return "/student/detail";
    }


    /** 학생 등록 화면
     * @return 학생 등록 화면 구현한 View 경로
     */
    @GetMapping("/student/add")
    public String add() {
        return "/student/add";
    }


    /**
     * 학생 등록 처리
     * Action 페이지들은 View를 사용하지 않고 다른 페이지로 이동해야 하므로 메서드 상단에 @ResponseBody를 적용하여 View 없이 직접 응답을 구현한다
     */
    @ResponseBody  // View를 사용하지 않음 (action페이지에 꼭 적용)
    @PostMapping("/student/add_ok")
    public void addOk( HttpServletRequest request,
        @RequestParam("name") String name,
        @RequestParam("userid") String userid,
        @RequestParam("grade") int grade,
        @RequestParam("idnum") String idnum,
        @RequestParam("birthdate") String birthdate,
        @RequestParam("tel") String tel,
        @RequestParam("height") int height,
        @RequestParam("weight") int weight,
        @RequestParam("deptno") int deptno,
        @RequestParam(value="profno", required=false) Integer profno) {

        String referer = request.getHeader("referer");

        if (referer==null || !referer.contains("/student")) {
            webHelper.badRequest("올바르지 않은 접근입니다");
            return;
        }
        
        Student student = new Student();
        student.setName(name);
        student.setUserid(userid);
        student.setGrade(grade);
        student.setIdnum(idnum);
        student.setBirthdate(birthdate);
        student.setTel(tel);
        student.setHeight(height);
        student.setWeight(weight);
        student.setDeptno(deptno);
        if (profno != null) {
            student.setProfno(profno);
        } else {
            student.setProfno(null);
        }

        try {
            studentService.addItem(student);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        webHelper.redirect("/student/detail/" + student.getStudno(), "등록되었습니다");
    }


    /**
     * 학생 삭제 처리
     * @param request
     * @param studno 학생번호
     */
    @ResponseBody
    @GetMapping("/student/delete/{studno}")
    public void delete( HttpServletRequest request,
        @PathVariable("studno") int studno) {
        
        String referer = request.getHeader("referer");

        if (referer==null || !referer.contains("/student")) {
            webHelper.badRequest("올바르지 않은 접근입니다");
            return;
        }

        Student student = new Student();
        student.setStudno(studno);

        try {
            studentService.deleteItem(student);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        webHelper.redirect("/student", "삭제되었습니다");        
    }


    /**
     * 학생 수정 페이지
     * @param model - Model 객체
     * @param studno - 학생 번호
     * @return View 페이지 경로
     */
    @GetMapping("/student/edit/{studno}")
    public String edit( Model model, @PathVariable("studno") int studno ) {

        Student params = new Student();
        params.setStudno(studno);

        Student student = null;

        try {
            student = studentService.getItem(params);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        model.addAttribute("student", student);

        return "/student/edit";
    }


    @ResponseBody
    @PostMapping("/student/edit_ok/{studno}")
    public void edit_ok(
        @PathVariable("studno") int studno,
        @RequestParam("name") String name,
        @RequestParam("userid") String userid,
        @RequestParam("grade") int grade,
        @RequestParam("idnum") String idnum,
        @RequestParam("birthdate") String birthdate,
        @RequestParam("tel") String tel,
        @RequestParam("height") int height,
        @RequestParam("weight") int weight,
        @RequestParam("deptno") int deptno,
        @RequestParam(value="profno", required=false) Integer profno) {
        
        // 수정에 사용될 값을 beans에 담는다
        Student student = new Student();
        student.setStudno(studno);
        student.setName(name);
        student.setUserid(userid);
        student.setGrade(grade);
        student.setIdnum(idnum);
        student.setBirthdate(birthdate);
        student.setTel(tel);
        student.setHeight(height);
        student.setWeight(weight);
        student.setDeptno(deptno);
        if (profno != null) {
            student.setProfno(profno);
        } else {
            student.setProfno(null);
        }

        try {
            student = studentService.editItem(student);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // 수정결과 확인을 위해 상세페이지로 이동
        webHelper.redirect("/student/detail/" + student.getStudno(), "수정되었습니다");
    }

}
