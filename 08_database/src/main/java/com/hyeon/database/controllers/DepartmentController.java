package com.hyeon.database.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.hyeon.database.exceptions.ServiceNoResultException;
import com.hyeon.database.helpers.WebHelper;
import com.hyeon.database.models.Department;
import com.hyeon.database.services.DepartmentService;

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
}
