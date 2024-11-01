package com.hyeon.crud.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.hyeon.crud.exceptions.ServiceNoResultException;
import com.hyeon.crud.helpers.PaginationHelper;
import com.hyeon.crud.helpers.WebHelper;
import com.hyeon.crud.models.Department;
import com.hyeon.crud.models.Professor;
import com.hyeon.crud.services.DepartmentService;
import com.hyeon.crud.services.ProfessorService;

@Controller
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private WebHelper webHelper;

    
    @GetMapping({"/department", "/department/list"})
    public String list(Model model,
        // 검색어 파라미터 (페이지가 처음 열릴 때는 값 없음. 필수(required)가 아님)
        @RequestParam(value="keyword", required=false) String keyword,
        @RequestParam(value="page", defaultValue="1") int nowPage) {
        
        int totalCount = 0;
        int listCount = 5;  // 한 페이지당 표시할 목록 수
        int pageCount = 3;  // 한 그룹당 표시할 페이지 번호 수

        // 페이지 번호 계산한 결과가 저장될 객체
        PaginationHelper pagination = null;

        // 조회 조건에 사용할 객체
        Department input = new Department();
        input.setDname(keyword);
        input.setLoc(keyword);

        List<Department> departments = null;

        try {
            // 전체 게시글 수 조회
            totalCount = departmentService.getCount(input);

            // 페이지 번호 계산 => 계산결과를 로그로 출력
            pagination = new PaginationHelper(nowPage, totalCount, listCount, pageCount);

            // SQL의 LIMIT절에서 사용될 값을 Beans의 static 변수에 저장
            Department.setOffset(pagination.getOffset());
            Department.setListCount(pagination.getListCount());

            departments = departmentService.getList(input);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
            return null;
        } catch (Exception e) {
            webHelper.serverError(e);
            return null;
        }

        model.addAttribute("departments", departments);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pagination", pagination);

        return "/department/list";
    }

    
    @GetMapping("/department/view/{deptNo}")
    public String view( Model model, @PathVariable("deptNo") int deptNo) {

        // 조회 조건에 사용할 변수를 Beans에 저장
        Department input = new Department();
        input.setDeptNo(deptNo);

        Professor input2 = new Professor();
        input2.setDeptno(deptNo);

        // 조회 결과를 저장할 객체 선언
        Department department = null;
        List<Professor> professors = null;

        try {
            // 데이터 조회
            department = departmentService.getItem(input);
            professors = professorService.getList(input2);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // View에 데이터 전달
        model.addAttribute("department", department);
        model.addAttribute("professors", professors);

        return "/department/view";
    }


    @GetMapping("/department/add")
    public String add() {
        return "department/add";
    }


    @GetMapping("/department/edit/{deptNo}")
    public String edit(Model model, @PathVariable("deptNo") int deptNo) {

        Department input = new Department();
        input.setDeptNo(deptNo);

        Department department = null;

        try {
            department = departmentService.getItem(input);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        model.addAttribute("department", department);

        return "/department/edit";
    }
}
