package kr.hossam.crud.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import kr.hossam.crud.helpers.Pagination;
import kr.hossam.crud.helpers.WebHelper;
import kr.hossam.crud.models.Department;
import kr.hossam.crud.models.Professor;
import kr.hossam.crud.services.DepartmentService;
import kr.hossam.crud.services.ProfessorService;

@Controller
public class DepartmentController {

    /** 학과 관리 서비스 객체 주입 */
    @Autowired
    private DepartmentService departmentService;

    /** 교수 관리 서비스 객체 주입 */
    @Autowired
    private ProfessorService professorService;

    /** WebHelper 주입 */
    @Autowired
    private WebHelper webHelper;


    // @GetMapping({ "/department", "/department/list" })
    // public String list() {
    //     return "department/list";
    // }

    @GetMapping({ "/department", "/department/list" })
    public String index(Model model,
            // 검색어 파라미터 (페이지가 청음 열릴 때는 값 없음. 필수(required)가 아님)
            @RequestParam(value = "keyword", required = false) String keyword,
            // 페이지 구현에서 사용할 현재 페이지 번호
            @RequestParam(value = "page", defaultValue = "1") int nowPage) {

        int totalCount = 0; // 전체 게시글 수
        int listCount = 10; // 한 페이지당 표시할 목록 수
        int pageCount = 5; // 한 그룹당 표시할 페이지 번호 수

        // 페이지 번호를 계산한 결과가 저장될 객체
        Pagination pagination = null;

        // 조회 조건에 사용할 객체
        Department input = new Department();
        input.setDname(keyword);
        input.setLoc(keyword);

        List<Department> output = null;

        try {
            // 전체 게시글 수 조회
            totalCount = departmentService.getCount(input);
            // 페이지 번호 계산 --> 계산결과를 로그로 출력될 것이다.
            pagination = new Pagination(nowPage, totalCount, listCount, pageCount);

            // SQL의 LIMIT절에서 사용될 값을 Beans의 static 변수에 저장
            Department.setOffset(pagination.getOffset());
            Department.setListCount(pagination.getListCount());

            output = departmentService.getList(input);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        model.addAttribute("departments", output);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pagination", pagination);

        return "/department/list";
    }

    @GetMapping("/department/view/{deptNo}")
    public String view(Model model, @PathVariable("deptNo") int deptNo) {
        // 조회 조건에 사용할 변수를 Beans에 저장
        Department input = new Department();
        input.setDeptNo(deptNo);

        Professor input2 = new Professor();
        input2.setDeptno(deptNo);

        // 조회 결과를 저장할 객체 선언
        Department output = null;
        List<Professor> output2 = null;

        try {
            // 데이터 조회
            output = departmentService.getItem(input);
            output2 = professorService.getList(input2);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // View에 데이터 전달
        model.addAttribute("department", output);
        model.addAttribute("professors", output2);
        return "/department/view";
    }

    @GetMapping("/department/add")
    public String add() {
        return "department/add";
    }

    @GetMapping("/department/edit/{deptNo}")
    public String edit(Model model, @PathVariable("deptNo") int deptNo) {
        // 파라미터로 받은 PK값을 beans객체에 담는다.
        // --> 검색 조건으로 사용하기 위함
        Department input = new Department();
        input.setDeptNo(deptNo);

        // 수정할 데이터의 현재 값을 조회한다.
        Department output = null;

        try {
            output = departmentService.getItem(input);
        } catch (Exception e) {
            webHelper.serverError(e);
        }

        // View에 데이터 전달
        model.addAttribute("department", output);

        return "/department/edit";
    }
}
