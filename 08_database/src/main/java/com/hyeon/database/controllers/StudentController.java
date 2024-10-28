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

import com.hyeon.database.helpers.PaginationHelper;
import com.hyeon.database.helpers.WebHelper;
import com.hyeon.database.models.Department;
import com.hyeon.database.models.Professor;
import com.hyeon.database.models.Student;
import com.hyeon.database.services.DepartmentService;
import com.hyeon.database.services.ProfessorService;
import com.hyeon.database.services.StudentService;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class StudentController {
    
    @Autowired
    private StudentService studentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private WebHelper webHelper;


    /**
     * 학생 목록 화면
     * @param model - 모델
     * @return 학생 목록 화면을 구현할 View 경로
     */
    @GetMapping("/student")
    public String index(Model model,
        @RequestParam(value="keyword", required = false) String keyword,
        @RequestParam(value="page", defaultValue = "1") int nowPage ) {
        
        int totalCount = 0;
        int listCount = 5;
        int pageCount = 3;

        PaginationHelper pagination = null;

        Student input = new Student();
        input.setName(keyword);
        input.setUserid(keyword);

        List<Student> students = null;

        try {
            totalCount = studentService.getCount(input);
            pagination = new PaginationHelper(nowPage, totalCount, listCount, pageCount);
            
            Student.setOffset(pagination.getOffset());
            Student.setListCount(pagination.getListCount());

            students = studentService.getList(input);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pagination", pagination);

        return "/student/index";
    }


    /** 상세 조회 페이지  */
    @GetMapping("/student/detail/{studno}")
    public String detail(Model model, @PathVariable("studno") int studno) {

        // 조회 조건에 사용할 변수를 Beans에 저장
        Student input = new Student();
        input.setStudno(studno);

        // 조회 결과를 저장할 객체 선언
        Student student = null;

        try {
            // 데이터 조회
            student = studentService.getItem(input);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // View에 데이터 전달
        model.addAttribute("student", student);

        return "/student/detail";
    }


    /** 학생 등록 화면 */
    @GetMapping("/student/add")
    public String add( Model model ) {
        List<Department> output = null;
        List<Professor> output2 = null;

        try {
            output = departmentService.getList(null);
            output2 = professorService.getList(null);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        model.addAttribute("departments", output);
        model.addAttribute("professors", output2);
        
        return "/student/add";
    }


    /** 학생 등록 처리 */
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
        
        Student input = new Student();
        input.setName(name);
        input.setUserid(userid);
        input.setGrade(grade);
        input.setIdnum(idnum);
        input.setBirthdate(birthdate);
        input.setTel(tel);
        input.setHeight(height);
        input.setWeight(weight);
        input.setDeptno(deptno);
        
        if (profno != null) {
            input.setProfno(profno);
        } else {
            input.setProfno(null);
        }

        try {
            studentService.addItem(input);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        webHelper.redirect("/student/detail/" + input.getStudno(), "등록되었습니다");
    }


    /** 학생 삭제 처리 */
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
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        webHelper.redirect("/student", "삭제되었습니다");        
    }


    /** 학생 수정 페이지 */
    @GetMapping("/student/edit/{studno}")
    public String edit( Model model, @PathVariable("studno") int studno ) {

        Student input = new Student();
        input.setStudno(studno);

        Student student = null;
        List<Department> departments = null;
        List<Professor> professors = null;

        try {
            student = studentService.getItem(input);
            departments = departmentService.getList(null);
            professors = professorService.getList(null);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        model.addAttribute("student", student);
        model.addAttribute("departments", departments);
        model.addAttribute("professors", professors);

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
            studentService.editItem(student);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // 수정결과 확인을 위해 상세페이지로 이동
        webHelper.redirect("/student/detail/" + student.getStudno(), "수정되었습니다");
    }

}
