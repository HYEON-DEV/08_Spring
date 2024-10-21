package com.hyeon.database.services.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.hyeon.database.exceptions.ServiceNoResultException;
import com.hyeon.database.models.Department;
import com.hyeon.database.models.Professor;
import com.hyeon.database.models.Student;
import com.hyeon.database.services.DepartmentService;


/**
 * 학과 관리 기능과 관련된 MyBatis Mapper를 간접적으로 호출하기 위한 기능 명세
 * 
 * (1) 모든 메서드를 재정의 한  직후 리턴값 먼저 정의
 */

public class DepartmentServiceImpl implements DepartmentService {

    /**
     * (2) MyBatis 의 Mapper 를 호출하기 위한 SqlSession 객체
     * => import org.apache.ibatis.session.SqlSession;
     */ 
    private SqlSession sqlSession; 

    /**(3) 생성자, SqlSession을 전달받는다 */
    public DepartmentServiceImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Department addItem(Department params) throws ServiceNoResultException, Exception {
        Department result = null;

        /**
         * insert문 수행
         * 리턴되는 값은 저장된 데이터 수
         * 생성된 PK는 파라미터로 전달된 params 객체의 적절한 멤버변수에 설정된다
         * => getter를 통해 취득 가능
         */
        int rows = sqlSession.insert("DepartmentMapper.insert", params);

        if (rows == 0){
            throw new ServiceNoResultException("저장된 데이터가 없습니다");
        }
        
        // params에 설정된 PK 값을 활용해 저장된 내용을 그대로 조회한다
        result = sqlSession.selectOne("DepartmentMapper.selectItem", params);

        return result;
    }

    @Override
    public Department editItem(Department params) throws ServiceNoResultException, Exception {
        Department result = null;

        int rows = sqlSession.update("DepartmentMapper.update", params);

        // 수정된 데이터가 없다면
        if (rows == 0){
            throw new ServiceNoResultException("수정된 데이터가 없습니다");
        }
        
        result = sqlSession.selectOne("DepartmentMapper.selectItem", params);

        return result;
    }

    @Override
    public int deleteItem(Department params) throws ServiceNoResultException, Exception {
        int result = 0;

        // 학과 데이터 삭제를 위해 참조관계에 있는 자식 데이터를 순서대로 삭제
        
        Student student = new Student();
        student.setDeptno(params.getDeptNo());
        sqlSession.delete("StudentMapper.deleteByDeptno", student);

        Professor professor = new Professor();
        professor.setDeptno(params.getDeptNo());
        sqlSession.delete("ProfessorMapper.deleteByDeptno", professor);

        result = sqlSession.delete("DepartmentMapper.delete", params);

        if (result==0) {
            throw new ServiceNoResultException("삭제된 데이터가 없습니다");
        }

        return result;
    }

    @Override
    public Department getItem(Department params) throws ServiceNoResultException, Exception {
        Department result = null;

        result = sqlSession.selectOne("DepartmentMapper.selectItem", params);

        if (result==null) {
            throw new ServiceNoResultException("조회된 데이터가 없습니다");
        }

        return result;
    }

    @Override
    public List<Department> getList(Department params) throws ServiceNoResultException, Exception {
        return sqlSession.selectList("DepartmentMapper.selectList", params);
    }
    
}
