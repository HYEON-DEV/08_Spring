package com.hyeon.database.services;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hyeon.database.exceptions.ServiceNoResultException;
import com.hyeon.database.models.Student;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class StudentServiceTest {
    
    @Autowired
    private StudentService studentService;


    @Test
    @DisplayName("학생 추가 테스트")
    void insertStudent() throws Exception {
        Student input = new Student();
        input.setName("이광호");
        input.setUserid("leekh");
        input.setGrade(4);
        input.setIdnum("9012161234567");
        input.setBirthdate("2023-04-11");
        input.setTel("010-1234-5678");
        input.setHeight(175);
        input.setWeight(100);
        input.setDeptno(101);
        input.setProfno(null);

        Student output = null;

        try {
            output = studentService.addItem(input);
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
    @DisplayName("학생 수정 테스트")
    void updateStudent() throws Exception {
        Student input = new Student();
        input.setStudno(10203);
        input.setName("이광호");
        input.setUserid("hossam");
        input.setGrade(4);
        input.setIdnum("9012161234567");
        input.setBirthdate("2023-04-11");
        input.setTel("010-1234-5679");
        input.setHeight(175);
        input.setWeight(100);
        input.setDeptno(101);
        input.setProfno(9901);

        Student output = null;

        try {
            output = studentService.editItem(input);
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
    @DisplayName("학생 삭제 테스트")
    void deleteStudent() throws Exception {
        Student input = new Student();
        input.setStudno(10204);
        
        try {
            studentService.deleteItem(input);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }
    }


    @Test
    @DisplayName("단일행 학생 조회 테스트")
    void selectOneStudent() throws Exception {
        Student input = new Student();
        input.setStudno(20109);
    
        Student output = null;

        try {
            output = studentService.getItem(input);
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
    @DisplayName("학생 목록 조회 테스트")
    void selectListStudent() throws Exception {
        List<Student> output = null;

        try {
            output = studentService.getList(null);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }

        if (output != null) {
            for(Student item : output) {
                log.debug("output : " + item.toString());
            }
        }
  
    }

}
