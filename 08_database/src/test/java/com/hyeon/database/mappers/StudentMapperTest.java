package com.hyeon.database.mappers;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hyeon.database.models.Student;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class StudentMapperTest {
    
    // 테스트 클래스에서는 객체 주입을 사용해야한다
    @Autowired
    private StudentMapper studentMapper;

    @Test
    @DisplayName("학생 추가 테스트")
    void insertStudent() {
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

        int output = studentMapper.insert(input);
        
        log.debug("output: " + output);
        log.debug("new studno: " + input.getStudno());
    }

    @Test
    @DisplayName("학생 수정 테스트")
    void updateStudent() {
        Student input = new Student();
        input.setStudno(20110);
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

        int output = studentMapper.update(input);
        
        log.debug("output: " + output);
    }
 
    @Test
    @DisplayName("학생 삭제 테스트")
    void deleteStudent() {
        Student input = new Student();
        input.setStudno(20110);

        int output = studentMapper.delete(input);
        
        log.debug("output: " + output);
    }

    @Test
    @DisplayName("하나의 학생 조회 테스트")
    void selectOneStudent() {
        Student input = new Student();
        input.setStudno(20109);

        Student output = studentMapper.selectItem(input);
        
        log.debug("output: " + output.toString());
    }

    @Test
    @DisplayName("학생 목록 조회 테스트")
    void selectListStudent() {
        List<Student> output = studentMapper.selectList(null);

        for (Student item : output) {
            log.debug("output: " + item.toString());
        }
    }
    
}
