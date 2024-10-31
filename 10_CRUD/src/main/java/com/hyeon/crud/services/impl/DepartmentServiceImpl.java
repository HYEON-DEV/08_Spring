package com.hyeon.crud.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyeon.crud.exceptions.ServiceNoResultException;
import com.hyeon.crud.mappers.DepartmentMapper;
import com.hyeon.crud.mappers.ProfessorMapper;
import com.hyeon.crud.mappers.StudentMapper;
import com.hyeon.crud.models.Department;
import com.hyeon.crud.models.Professor;
import com.hyeon.crud.models.Student;
import com.hyeon.crud.services.DepartmentService;


@Service
public class DepartmentServiceImpl implements DepartmentService {

    /** SQL문 구현하고 있는 Mapper 객체 주입 */
    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private ProfessorMapper professorMapper;

    @Autowired
    private StudentMapper studentMapper;


    @Override
    public Department addItem(Department input) throws ServiceNoResultException, Exception {
        int rows = departmentMapper.insert(input);

        if (rows == 0){
            throw new ServiceNoResultException("저장된 데이터가 없습니다");
        }
        
        return departmentMapper.selectItem(input);
    }


    @Override
    public Department editItem(Department input) throws ServiceNoResultException, Exception {
        int rows = departmentMapper.update(input);

        if (rows == 0){
            throw new ServiceNoResultException("수정된 데이터가 없습니다");
        }
        
        return departmentMapper.selectItem(input);
    }


    @Override
    public int deleteItem(Department input) throws ServiceNoResultException, Exception {
        
        Student student = new Student();
        student.setDeptno(input.getDeptNo());
        studentMapper.deleteByDeptno(student);

        Professor professor = new Professor();
        professor.setDeptno(input.getDeptNo());
        professorMapper.deleteByDeptno(professor);

        int rows = departmentMapper.delete(input);

        if (rows==0) {
            throw new ServiceNoResultException("삭제된 데이터가 없습니다");
        }

        return rows;
    }


    @Override
    public Department getItem(Department input) throws ServiceNoResultException, Exception {
        Department output = departmentMapper.selectItem(input);

        if (output==null) {
            throw new ServiceNoResultException("조회된 데이터가 없습니다");
        }

        return output;
    }

    
    @Override
    public List<Department> getList(Department input) throws ServiceNoResultException, Exception {
        return departmentMapper.selectList(input);
    }


    @Override
    public int getCount(Department input) throws Exception {
        return departmentMapper.selectCount(input);
    }
    
}
