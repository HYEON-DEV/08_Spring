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
import com.hyeon.crud.models.Professor;
//import com.hyeon.crud.models.Department;
//import com.hyeon.crud.services.DepartmentService;
import com.hyeon.crud.services.ProfessorService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class ProfessorRestController {
    
    @Autowired
    private ProfessorService professorService;

    //@Autowired
    //private DepartmentService departmentService;

    @Autowired
    private RestHelper restHelper;



    /** 교수 목록 화면 */
    @GetMapping("/api/professor")
    public Map<String,Object> getList(
        @RequestParam(value="keyword", required = false) String keyword,
        @RequestParam(value="page", defaultValue = "1") int nowPage ) {

        int totalCount = 0;
        int listCount = 5;
        int pageCount = 3;

        PaginationHelper pagination = null;

        Professor input = new Professor();
        input.setName(keyword);
        input.setUserid(keyword);

        List<Professor> output = null;

        try {
            totalCount = professorService.getCount(input);
            pagination = new PaginationHelper(nowPage, totalCount, listCount, pageCount);
            
            Professor.setOffset(pagination.getOffset());
            Professor.setListCount(pagination.getListCount());

            output = professorService.getList(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("item", output);
        data.put("keyword", keyword);
        data.put("pagination", pagination);

        return restHelper.sendJson(data);
    }



    /** 교수 상세조회 */
    @GetMapping("/api/professor/{profno}")
    public Map<String,Object> detail(@PathVariable("profno") int profno) {

        // 조회 조건에 사용할 변수를 Beans에 저장
        Professor input = new Professor();
        input.setProfno(profno);

        // 조회 결과를 저장할 객체 선언
        Professor professor = null;

        try {
            // 데이터 조회
            professor = professorService.getItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("item", professor);

        return restHelper.sendJson(data);
    }



    /** 교수 등록 */
    @PostMapping("/api/professor")
    public Map<String,Object> addOk( HttpServletRequest request,
        @RequestParam("name") String name,
        @RequestParam("userid") String userid,
        @RequestParam("position") String position,
        @RequestParam("sal") int sal,
        @RequestParam("hiredate") String hiredate,
        @RequestParam(value="comm", required=false) Integer comm,
        @RequestParam("deptno") int deptno) {

        Professor input = new Professor();
        input.setName(name);
        input.setUserid(userid);
        input.setPosition(position);
        input.setSal(sal);
        input.setHiredate(hiredate);
        input.setDeptno(deptno);

        if (comm != null) {
            input.setComm(comm);
        } else {
            input.setComm(null);
        }

        Professor output = null;

        try {
            output = professorService.addItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String,Object> data = new LinkedHashMap<String, Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }



    /** 교수 삭제 */
    @DeleteMapping("/api/professor/{profno}")
    public Map<String,Object> delete( HttpServletRequest request,
        @PathVariable("profno") int profno) {
        
        Professor input = new Professor();
        input.setProfno(profno);

        try {
            professorService.deleteItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        return restHelper.sendJson();
    }



    /** 교수 수정 */
    @PutMapping("/api/professor/{profno}")
    public Map<String,Object> edit_ok(
        @PathVariable("profno") int profno,
        @RequestParam("name") String name,
        @RequestParam("userid") String userid,
        @RequestParam("position") String position,
        @RequestParam("sal") int sal,
        @RequestParam("hiredate") String hiredate,
        @RequestParam(value="comm", required=false) Integer comm,
        @RequestParam("deptno") int deptno) {
        
        // 수정에 사용될 값을 beans에 담는다
        Professor input = new Professor();
        input.setProfno(profno);
        input.setName(name);
        input.setUserid(userid);
        input.setPosition(position);
        input.setSal(sal);
        input.setHiredate(hiredate);
        input.setDeptno(deptno);

        if (comm != null) {
            input.setComm(comm);
        } else {
            input.setComm(null);
        }

        Professor output = null;

        try {
            output = professorService.editItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }

}
