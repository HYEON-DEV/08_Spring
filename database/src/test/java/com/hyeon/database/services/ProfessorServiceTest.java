package com.hyeon.database.services;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hyeon.database.exceptions.ServiceNoResultException;
import com.hyeon.database.models.Professor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ProfessorServiceTest {
    
    @Autowired
    private ProfessorService professorService;


    @Test
    @DisplayName("교수 추가 테스트")
    void insertProfessor() throws Exception {
        Professor input = new Professor();
        input.setName("이광호");
        input.setUserid("leekh");
        input.setPosition("교수");
        input.setSal(500);
        input.setHiredate("2023-04-11");
        input.setComm(100);
        input.setDeptno(101);

        Professor output = null;

        try {
            output = professorService.addItem(input);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }

        if (output != null) {
            log.debug("output : " + output.toString());
        }
    }


    @Test
    @DisplayName("교수 수정 테스트")
    void updateProfessor() throws Exception {
        Professor input = new Professor();
        input.setProfno(9901);
        input.setName("이광호");
        input.setUserid("hossam");
        input.setPosition("교수");
        input.setSal(600);
        input.setHiredate("2023-04-11");
        input.setComm(90);
        input.setDeptno(101);

        Professor output = null;

        try {
            output = professorService.editItem(input);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }

        if (output != null) {
            log.debug("output : " + output.toString());
        }
    }


    @Test
    @DisplayName("교수 삭제 테스트")
    void deleteProfessor() throws Exception {
        Professor input = new Professor();
        input.setProfno(9926);
        
        try {
            professorService.deleteItem(input);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }
    }


    @Test
    @DisplayName("단일행 교수 조회 테스트")
    void selectOneProfessor() throws Exception {
        Professor input = new Professor();
        input.setProfno(9920);
    
        Professor output = null;

        try {
            output = professorService.getItem(input);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }

        if (output != null) {
            log.debug("output : " + output.toString());
        }
    }


    @Test
    @DisplayName("교수 목록 조회 테스트")
    void selectListProfessor() throws Exception {
        List<Professor> output = null;

        try {
            output = professorService.getList(null);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }

        if (output != null) {
            for(Professor item : output) {
                log.debug("output : " + item.toString());
            }
        }
        /* 
        Professor input = new Professor();
        input.setDname("교수");

        try {
            output = professorService.getList(input);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }

        if (output != null) {
            for(Professor item : output) {
                log.debug("output : " + item.toString());
            }
        } 
        */
    }

}
