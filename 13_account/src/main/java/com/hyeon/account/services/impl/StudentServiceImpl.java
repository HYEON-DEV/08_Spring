package com.hyeon.account.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyeon.account.mappers.StudentMapper;
import com.hyeon.account.models.Student;
import com.hyeon.account.services.StudentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StudentServiceImpl implements StudentService {

    /** SQL문 구현하고 있는 Mapper 객체 주입 */
    @Autowired
    private StudentMapper studentMapper;


    @Override
    public Student addItem(Student input) throws Exception {
        int rows = 0;

        try {
            rows = studentMapper.insert(input);

            if (rows == 0){
                throw new Exception("----- 저장된 데이터가 없습니다 -----");
            }
        } catch (Exception e) {
            log.error("----- 데이터 저장 실패 -----", e);
            throw e;
        }
        
        return studentMapper.selectItem(input);
    }


    @Override
    public Student editItem(Student input) throws Exception {
        int rows = 0;

        try {
            rows = studentMapper.update(input);

            if (rows == 0){
                throw new Exception("----- 수정된 데이터가 없습니다 -----");
            }
        } catch (Exception e) {
            log.error("----- 데이터 수정 실패 -----", e);
            throw e;
        }
        return studentMapper.selectItem(input);
    }


    @Override
    public int deleteItem(Student input) throws Exception {
        int rows = 0;

        try {
            rows = studentMapper.delete(input);

            if (rows == 0){
                throw new Exception("----- 삭제된 데이터가 없습니다 -----");
            }
        } catch (Exception e) {
            log.error("----- 데이터 삭제 실패 -----", e);
            throw e;
        }
        
        return rows;
    }


    @Override
    public Student getItem(Student input) throws Exception {
        Student output = null;

        try {
            output = studentMapper.selectItem(input);

            if (output == null){
                throw new Exception("----- 조회된 데이터가 없습니다 -----");
            }
        } catch (Exception e) {
            log.error("----- 데이터 조회 실패 -----", e);
            throw e;
        }

        return output;
    }

    
    @Override
    public List<Student> getList(Student input) throws Exception {
        List<Student> output = null;

        try {
            output = studentMapper.selectList(input);
        } catch (Exception e) {
            log.error("----- 데이터 조회 실패 -----", e);
            throw e;
        }

        return output;
    }


    @Override
    public int getCount(Student input) throws Exception {
        int output = 0;

        try {
            output = studentMapper.selectCount(input);
        } catch (Exception e) {
            log.error("----- 데이터 집계 실패 -----", e);
            throw e;
        }
        
        return output;
    }
    
}
