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
import kr.hossam.crud.models.Professor;
import kr.hossam.crud.services.ProfessorService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfessorRestController {

    /** 교수 관리 서비스 객체 주입 */
    @Autowired
    private ProfessorService professorService;

    /** RestHelper 주입 */
    @Autowired
    private RestHelper restHelper;

    /**
     * 교수 목록 API
     *
     * @param model 모델
     * @return 교수 목록을 포함하는 JSON 데이터
     */
    @GetMapping("/api/professor")
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
        Professor input = new Professor();
        input.setName(keyword);
        input.setUserid(keyword);

        List<Professor> output = null;

        try {
            // 전체 게시글 수 조회
            totalCount = professorService.getCount(input);
            // 페이지 번호 계산 --> 계산결과를 로그로 출력될 것이다.
            pagination = new Pagination(nowPage, totalCount, listCount, pageCount);

            // SQL의 LIMIT절에서 사용될 값을 Beans의 static 변수에 저장
            Professor.setOffset(pagination.getOffset());
            Professor.setListCount(pagination.getListCount());

            output = professorService.getList(input);
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
     * 교수 상세 API
     *
     * @param model  모델
     * @param profno 교수 번호
     * @return 교수 상세 정보를 포함하는 JSON 데이터
     */
    @GetMapping("/api/professor/{profno}")
    public Map<String, Object> get(
            @PathVariable("profno") int profno) {

        // 조회 조건에 사용할 변수를 Beans에 저장
        Professor input = new Professor();
        input.setProfno(profno);

        // 조회 결과를 저장할 객체 선언
        Professor output = null;

        try {
            // 데이터 조회
            output = professorService.getItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }

    /**
     * 교수 등록 API
     *
     * @param dname 교수 이름
     * @param loc   교수 위치
     *
     * @return 등록 결과를 포함하는 JSON 데이터
     */
    @PostMapping("/api/professor")
    public Map<String, Object> post(
            @RequestParam("name") String name,
            @RequestParam("userid") String userid,
            @RequestParam("position") String position,
            @RequestParam("sal") int sal,
            @RequestParam("hiredate") String hiredate,
            @RequestParam("comm") Integer comm,
            @RequestParam("deptno") int deptno) {

        // 저장할 값들을 Beans에 담는다.
        Professor input = new Professor();
        input.setName(name);
        input.setUserid(userid);
        input.setPosition(position);
        input.setSal(sal);
        input.setHiredate(hiredate);
        input.setComm(comm);
        input.setDeptno(deptno);

        Professor output = null;

        try {
            output = professorService.addItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }

    /**
     * 교수 삭제 API
     *
     * @param profno 교수 번호
     */
    @DeleteMapping("/api/professor/{profno}")
    public Map<String, Object> delete(
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

    /**
     * 교수 수정 API
     *
     * @param profno 교수 번호
     * @param dname  교수 이름
     * @param loc    교수 위치
     *
     * @return 수정 결과를 포함하는 JSON 데이터
     */
    @PutMapping("/api/professor/{profno}")
    public Map<String, Object> put(
            @PathVariable("profno") int profno,
            @RequestParam("name") String name,
            @RequestParam("userid") String userid,
            @RequestParam("position") String position,
            @RequestParam("sal") int sal,
            @RequestParam("hiredate") String hiredate,
            @RequestParam("comm") Integer comm,
            @RequestParam("deptno") int deptno) {

        // 수정에 사용될 값을 Beans에 담는다.
        Professor input = new Professor();
        input.setProfno(profno);
        input.setName(name);
        input.setUserid(userid);
        input.setPosition(position);
        input.setSal(sal);
        input.setHiredate(hiredate);
        input.setComm(comm);
        input.setDeptno(deptno);

        Professor output = null;

        // 데이터를 수정한다.
        try {
            output = professorService.editItem(input);
        } catch (Exception e) {
            return restHelper.serverError(e);
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("item", output);

        return restHelper.sendJson(data);
    }
}
