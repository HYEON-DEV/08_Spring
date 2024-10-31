package com.hyeon.crud.controllers.restful;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyeon.crud.helpers.PaginationHelper;
import com.hyeon.crud.helpers.RestHelper;
import com.hyeon.crud.models.Department;
import com.hyeon.crud.services.DepartmentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class DepartmentRestController {
    
    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private RestHelper restHelper;


    @GetMapping("/api/department")
    public Map<String,Object> getList(
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
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("keyword", keyword);
        data.put("item", departments);
        data.put("pagination", pagination);

        return restHelper.sendJson(data);
    }



    @GetMapping("/api/department/{deptNo}")
    public Map<String,Object> detail(Model model, @PathVariable("deptNo") int deptNo) {

        // 조회 조건에 사용할 변수를 Beans에 저장
        Department input = new Department();
        input.setDeptNo(deptNo);

        // 조회 결과를 저장할 객체 선언
        Department department = null;

        try {
            // 데이터 조회
            department = departmentService.getItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("item", department);

        return restHelper.sendJson(data);
    }



    // @ResponseBody  // View를 사용하지 않음 (action페이지에 꼭 적용)
    @PostMapping("/api/department")
    public Map<String,Object> addOk( HttpServletRequest request,
        @RequestParam("dname") String dname,
        @RequestParam("loc") String loc) {
              
        Department input = new Department();
        input.setDname(dname);
        input.setLoc(loc);

        Department output = null;

        try {
            output = departmentService.addItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String,Object> data = new LinkedHashMap<String, Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }


    
    @DeleteMapping("/api/department/{deptNo}")
    public Map<String,Object> delete( HttpServletRequest request,
        @PathVariable("deptNo") int deptNo) {

        Department input = new Department();
        input.setDeptNo(deptNo);

        try {
            departmentService.deleteItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        return restHelper.sendJson();
    }


    @PutMapping("/api/department/{deptNo}")
    public Map<String,Object> edit_ok(
        @PathVariable("deptNo") int deptNo,
        @RequestParam("dname") String dname,
        @RequestParam("loc") String loc) {
        
        // 수정에 사용될 값을 beans에 담는다
        Department input = new Department();
        input.setDeptNo(deptNo);
        input.setDname(dname);
        input.setLoc(loc);

        Department output = null;

        try {
            output = departmentService.editItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String,Object> data = new LinkedHashMap<String,Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }
}
