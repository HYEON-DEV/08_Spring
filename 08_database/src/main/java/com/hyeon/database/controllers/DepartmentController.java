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
import com.hyeon.database.models.Department;
import com.hyeon.database.services.DepartmentService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class DepartmentController {
    
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private WebHelper webHelper;


    /**
     * 학과 목록 화면
     * @param model - 모델
     * @return 학과 목록 화면을 구현할 View 경로
     */
    @GetMapping("/department")
    public String index(Model model) {
        List<Department> departments = null;

        try {
            departments = departmentService.getList(null);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
            return null;
        } catch (Exception e) {
            webHelper.serverError(e);
            return null;
        }

        model.addAttribute("departments", departments);

        return "/department/index";
    }


    /** 상세 조회 페이지
     * @param model 모델
     * @param deptNo 학과 번호
     * @return 학과 상세 화면을 구현한 View 경로
     */
    @GetMapping("/department/detail/{deptNo}")
    public String detail(Model model, @PathVariable("deptNo") int deptNo) {

        // 조회 조건에 사용할 변수를 Beans에 저장
        Department params = new Department();
        params.setDeptNo(deptNo);

        // 조회 결과를 저장할 객체 선언
        Department department = null;

        try {
            // 데이터 조회
            department = departmentService.getItem(params);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // View에 데이터 전달
        model.addAttribute("department", department);

        return "/department/detail";
    }


    /** 학과 등록 화면
     * @return 학과 등록 화면 구현한 View 경로
     */
    @GetMapping("/department/add")
    public String add() {
        return "/department/add";
    }


    /**
     * 학과 등록 처리
     * Action 페이지들은 View를 사용하지 않고 다른 페이지로 이동해야 하므로 메서드 상단에 @ResponseBody를 적용하여 View 없이 직접 응답을 구현한다
     * @param request
     * @param dname
     * @param loc
     */
    @ResponseBody  // View를 사용하지 않음 (action페이지에 꼭 적용)
    @PostMapping("/department/add_ok")
    public void addOk( HttpServletRequest request,
        @RequestParam("dname") String dname,
        @RequestParam("loc") String loc) {

        /* 정상적인 경로로 접근한 경우 이전 페이지 주소는
         * 1) http://localhost/department
         * 2) http://localhost/department/detail/학과번호
         */
        String referer = request.getHeader("referer");

        if (referer==null || !referer.contains("/department")) {
            webHelper.badRequest("올바르지 않은 접근입니다");
            return;
        }
        
        Department department = new Department();
        department.setDname(dname);
        department.setLoc(loc);

        try {
            departmentService.addItem(department);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        /* Insert, Update, Delete 처리 수행하는 경우 리다이렉트로 이동
         * Insert 결과를 확인할 수 있는 상세 페이지로 이동해야 한다
         * 상세 페이지에 조회 대상의 PK 값을 전달해야 한다
         */
        webHelper.redirect("/department/detail/" + department.getDeptNo(), "등록되었습니다");

    }


    /**
     * 학과 삭제 처리
     * @param request
     * @param deptNo 학과번호
     */
    @ResponseBody
    @GetMapping("/department/delete/{deptNo}")
    public void delete( HttpServletRequest request,
        @PathVariable("deptNo") int deptNo) {
        
        String referer = request.getHeader("referer");

        if (referer==null || !referer.contains("/department")) {
            webHelper.badRequest("올바르지 않은 접근입니다");
            return;
        }

        Department department = new Department();
        department.setDeptNo(deptNo);

        try {
            departmentService.deleteItem(department);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        webHelper.redirect("/department", "삭제되었습니다");        
    }

    
    /**
     * 학과 수정 페이지
     * @param model - Model 객체
     * @param deptNo - 학과 번호
     * @return View 페이지 경로
     */
    @GetMapping("/department/edit/{deptNo}")
    public String edit( Model model, @PathVariable("deptNo") int deptNo ) {

        Department params = new Department();
        params.setDeptNo(deptNo);

        Department department = null;

        try {
            department = departmentService.getItem(params);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        model.addAttribute("department", department);

        return "/department/edit";
    }


    @ResponseBody
    @PostMapping("/department/edit_ok/{deptNo}")
    public void edit_ok(
        @PathVariable("deptNo") int deptNo,
        @RequestParam("dname") String dname,
        @RequestParam("loc") String loc) {
        
        // 수정에 사용될 값을 beans에 담는다
        Department department = new Department();
        department.setDeptNo(deptNo);
        department.setDname(dname);
        department.setLoc(loc);

        try {
            department = departmentService.editItem(department);
        } catch (ServiceNoResultException e) {
            webHelper.serverError(e);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // 수정결과 확인을 위해 상세페이지로 이동
        webHelper.redirect("/department/detail/" + department.getDeptNo(), "수정되었습니다");
    }
}
