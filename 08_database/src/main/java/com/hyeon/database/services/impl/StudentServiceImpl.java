package com.hyeon.database.services.impl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.hyeon.database.exceptions.ServiceNoResultException;

import com.hyeon.database.models.Student;

import com.hyeon.database.services.StudentService;


/**
 * 학생 관리 기능과 관련된 MyBatis Mapper를 간접적으로 호출하기 위한 기능 명세
 * 
 * (1) 모든 메서드를 재정의 한  직후 리턴값 먼저 정의
 */

public class StudentServiceImpl implements StudentService {

    /**
     * (2) MyBatis 의 Mapper 를 호출하기 위한 SqlSession 객체
     * => import org.apache.ibatis.session.SqlSession;
     */ 
    private SqlSession sqlSession; 

    /**(3) 생성자, SqlSession을 전달받는다 */
    public StudentServiceImpl(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public Student addItem(Student params) throws ServiceNoResultException, Exception {
        Student result = null;

        /**
         * insert문 수행
         * 리턴되는 값은 저장된 데이터 수
         * 생성된 PK는 파라미터로 전달된 params 객체의 적절한 멤버변수에 설정된다
         * => getter를 통해 취득 가능
         */
        int rows = sqlSession.insert("StudentMapper.insert", params);

        if (rows == 0){
            throw new ServiceNoResultException("저장된 데이터가 없습니다");
        }
        
        // params에 설정된 PK 값을 활용해 저장된 내용을 그대로 조회한다
        result = sqlSession.selectOne("StudentMapper.selectItem", params);

        return result;
    }

    @Override
    public Student editItem(Student params) throws ServiceNoResultException, Exception {
        Student result = null;

        int rows = sqlSession.update("StudentMapper.update", params);

        // 수정된 데이터가 없다면
        if (rows == 0){
            throw new ServiceNoResultException("수정된 데이터가 없습니다");
        }
        
        result = sqlSession.selectOne("StudentMapper.selectItem", params);

        return result;
    }

    @Override
    public int deleteItem(Student params) throws ServiceNoResultException, Exception {
        int result = 0;
          
        result = sqlSession.delete("StudentMapper.delete", params);

        if (result==0) {
            throw new ServiceNoResultException("삭제된 데이터가 없습니다");
        }

        return result;
    }

    @Override
    public Student getItem(Student params) throws ServiceNoResultException, Exception {
        Student result = null;

        result = sqlSession.selectOne("StudentMapper.selectItem", params);

        if (result==null) {
            throw new ServiceNoResultException("조회된 데이터가 없습니다");
        }

        return result;
    }

    @Override
    public List<Student> getList(Student params) throws ServiceNoResultException, Exception {
        return sqlSession.selectList("StudentMapper.selectList", params);
    }
    
}
