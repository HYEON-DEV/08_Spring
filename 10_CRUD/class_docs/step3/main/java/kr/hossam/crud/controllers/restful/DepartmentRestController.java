package kr.hossam.crud.controllers.restful;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import kr.hossam.crud.helpers.Pagination;
import kr.hossam.crud.helpers.RestHelper;
import kr.hossam.crud.models.Department;
import kr.hossam.crud.services.DepartmentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentRestController {

    /** 학과 관리 서비스 객체 주입 */
    @Autowired
    private DepartmentService departmentService;

    /** RestHelper 주입 */
    @Autowired
    private RestHelper restHelper;

    /**
     * 학과 목록 API
     *
     * @param model 모델
     * @return 학과 목록을 포함하는 JSON 데이터
     */
    @GetMapping("/api/department")
    public Map<String, Object> getList(
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
            return restHelper.serverError(e);
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("keyword", keyword);
        data.put("item", output);
        data.put("pagination", pagination);

        return restHelper.sendJson(data);
    }

    /**
     * 학과 상세 API
     *
     * @param model  모델
     * @param deptNo 학과 번호
     * @return 학과 상세 정보를 포함하는 JSON 데이터
     */
    @GetMapping("/api/department/{deptNo}")
    public Map<String, Object> get(
            @PathVariable("deptNo") int deptNo) {

        // 조회 조건에 사용할 변수를 Beans에 저장
        Department input = new Department();
        input.setDeptNo(deptNo);

        // 조회 결과를 저장할 객체 선언
        Department output = null;

        try {
            // 데이터 조회
            output = departmentService.getItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }

    /**
     * 학과 등록 API
     *
     * @param dname 학과 이름
     * @param loc   학과 위치
     *
     * @return 등록 결과를 포함하는 JSON 데이터
     */
    @PostMapping("/api/department")
    public Map<String, Object> post(
            @RequestParam("dname") String dname,
            @RequestParam("loc") String loc) {
        // 저장할 값들을 Beans에 담는다.
        Department input = new Department();
        input.setDname(dname);
        input.setLoc(loc);

        Department output = null;

        try {
            output = departmentService.addItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }

    /**
     * 학과 삭제 API
     *
     * @param deptNo 학과 번호
     */
    @DeleteMapping("/api/department/{deptNo}")
    public Map<String, Object> delete(
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

    /**
     * 학과 수정 API
     *
     * @param deptNo 학과 번호
     * @param dname  학과 이름
     * @param loc    학과 위치
     *
     * @return 수정 결과를 포함하는 JSON 데이터
     */
    @PutMapping("/api/department/{deptNo}")
    public Map<String, Object> put(
            @PathVariable("deptNo") int deptNo,
            @RequestParam("dname") String dname,
            @RequestParam("loc") String loc) {

        // 수정에 사용될 값을 Beans에 담는다.
        Department input = new Department();
        input.setDeptNo(deptNo);
        input.setDname(dname);
        input.setLoc(loc);

        Department output = null;

        // 데이터를 수정한다.
        try {
            output = departmentService.editItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }
}
