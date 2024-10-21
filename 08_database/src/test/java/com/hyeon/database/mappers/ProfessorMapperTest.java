package com.hyeon.database.mappers;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hyeon.database.models.Professor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ProfessorMapperTest {
    
    // 테스트 클래스에서는 객체 주입을 사용해야한다
    @Autowired
    private ProfessorMapper professorMapper;

    @Test
    @DisplayName("교수 추가 테스트")
    void insertProfessor() {
        Professor input = new Professor();
        input.setName("이광호");
        input.setUserid("leekh");
        input.setPosition("교수");
        input.setSal(500);
        input.setHiredate("2023-04-11");
        input.setComm(100);
        input.setDeptno(101);

        int output = professorMapper.insert(input);
        
        log.debug("output: " + output);
        log.debug("new profno: " + input.getProfno());
    }

    @Test
    @DisplayName("교수 수정 테스트")
    void updateProfessor() {
        Professor input = new Professor();
        input.setProfno(9925);
        input.setName("이광호");
        input.setUserid("hossam");
        input.setPosition("교수");
        input.setSal(600);
        input.setHiredate("2023-04-11");
        input.setComm(90);
        input.setDeptno(101);

        int output = professorMapper.update(input);
        
        log.debug("output: " + output);
    }
 
    @Test
    @DisplayName("교수 삭제 테스트")
    void deleteProfessor() {
        Professor input = new Professor();
        input.setProfno(9925);

        int output = professorMapper.delete(input);
        
        log.debug("output: " + output);
    }

    @Test
    @DisplayName("하나의 교수 조회 테스트")
    void selectOneProfessor() {
        Professor input = new Professor();
        input.setProfno(9920);

        Professor output = professorMapper.selectItem(input);
        
        log.debug("output: " + output.toString());
    }

    @Test
    @DisplayName("교수 목록 조회 테스트")
    void selectListProfessor() {
        List<Professor> output = professorMapper.selectList(null);

        for (Professor item : output) {
            log.debug("output: " + item.toString());
        }
    }
    
}
