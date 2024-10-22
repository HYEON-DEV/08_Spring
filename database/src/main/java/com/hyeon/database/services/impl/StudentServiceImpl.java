package com.hyeon.database.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyeon.database.exceptions.ServiceNoResultException;
import com.hyeon.database.mappers.StudentMapper;
import com.hyeon.database.models.Student;
import com.hyeon.database.services.StudentService;


@Service
public class StudentServiceImpl implements StudentService {

    /** SQL문 구현하고 있는 Mapper 객체 주입 */
    @Autowired
    private StudentMapper studentMapper;


    @Override
    public Student addItem(Student input) throws ServiceNoResultException, Exception {
        int rows = studentMapper.insert(input);

        if (rows == 0){
            throw new ServiceNoResultException("저장된 데이터가 없습니다");
        }
        
        return studentMapper.selectItem(input);
    }


    @Override
    public Student editItem(Student input) throws ServiceNoResultException, Exception {
        int rows = studentMapper.update(input);

        if (rows == 0){
            throw new ServiceNoResultException("수정된 데이터가 없습니다");
        }
        
        return studentMapper.selectItem(input);
    }


    @Override
    public int deleteItem(Student input) throws ServiceNoResultException, Exception {
        
        int rows = studentMapper.delete(input);

        if (rows==0) {
            throw new ServiceNoResultException("삭제된 데이터가 없습니다");
        }

        return rows;
    }


    @Override
    public Student getItem(Student input) throws ServiceNoResultException, Exception {
        Student output = studentMapper.selectItem(input);

        if (output==null) {
            throw new ServiceNoResultException("조회된 데이터가 없습니다");
        }

        return output;
    }

    
    @Override
    public List<Student> getList(Student input) throws ServiceNoResultException, Exception {
        return studentMapper.selectList(input);
    }
    
}
