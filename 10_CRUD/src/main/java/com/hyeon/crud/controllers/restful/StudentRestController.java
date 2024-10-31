package com.hyeon.crud.controllers.restful;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyeon.crud.helpers.PaginationHelper;
import com.hyeon.crud.helpers.RestHelper;
//import com.hyeon.crud.models.Department;
//import com.hyeon.crud.models.Professor;
import com.hyeon.crud.models.Student;
//import com.hyeon.crud.services.DepartmentService;
//import com.hyeon.crud.services.ProfessorService;
import com.hyeon.crud.services.StudentService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
public class StudentRestController {
    
    @Autowired
    private StudentService studentService;

    //@Autowired
    //private DepartmentService departmentService;

    //@Autowired
    //private ProfessorService professorService;

    @Autowired
    private RestHelper restHelper;



    /** 학생 목록 화면 */
    @GetMapping("/api/student")
    public Map<String,Object> getList(
        @RequestParam(value="keyword", required = false) String keyword,
        @RequestParam(value="page", defaultValue = "1") int nowPage ) {
        
        int totalCount = 0;
        int listCount = 5;
        int pageCount = 3;

        PaginationHelper pagination = null;

        Student input = new Student();
        input.setName(keyword);
        input.setUserid(keyword);

        List<Student> output = null;

        try {
            totalCount = studentService.getCount(input);
            pagination = new PaginationHelper(nowPage, totalCount, listCount, pageCount);
            
            Student.setOffset(pagination.getOffset());
            Student.setListCount(pagination.getListCount());

            output = studentService.getList(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("item", output);
        data.put("keyword", keyword);
        data.put("pagination", pagination);

        return restHelper.sendJson(data);
    }


    /** 상세 조회  */
    @GetMapping("/api/student/{studno}")
    public Map<String,Object> detail(@PathVariable("studno") int studno) {

        // 조회 조건에 사용할 변수를 Beans에 저장
        Student input = new Student();
        input.setStudno(studno);

        // 조회 결과를 저장할 객체 선언
        Student student = null;

        try {
            // 데이터 조회
            student = studentService.getItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("item", student);

        return restHelper.sendJson(data);
    }



    /** 학생 등록 */
    @PostMapping("/api/student")
    public Map<String,Object> addOk( HttpServletRequest request,
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

        Student output = null;

        try {
            output = studentService.addItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String,Object> data = new LinkedHashMap<String, Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }



    /** 학생 삭제 */
    @DeleteMapping("/api/student/{studno}")
    public Map<String,Object> delete( HttpServletRequest request,
        @PathVariable("studno") int studno) {

        Student student = new Student();
        student.setStudno(studno);

        try {
            studentService.deleteItem(student);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        return restHelper.sendJson();
    }



    /** 학생 수정 */
    @PutMapping("/api/student/{studno}")
    public Map<String,Object> edit_ok(
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

        Student output = null;

        try {
            output = studentService.editItem(student);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }

}
