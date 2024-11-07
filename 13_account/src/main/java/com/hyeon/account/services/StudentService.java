package com.hyeon.account.services;

import java.util.List;

import com.hyeon.account.models.Student;


public interface StudentService {
    /**
     * 학생 정보를 새로 저장하고 저장된 정보를 조회하여 리턴한다
     * @param input - 저장할 정보를 담고 있는 Beans
     * @return Student - 저장된 데이터
     * @throws Exception - SQL 처리에 실패한 경우
     */
    public Student addItem(Student input) throws Exception;

    /**
     * 학생 정보를 수정하고 수정된 정보를 조회하여 리턴한다
     * @param input - 수정할 정보를 담고 있는 Beans
     * @return Student - 수정된 데이터
     * @throws Exception - SQL 처리에 실패한 경우
     */
    public Student editItem(Student input) throws Exception;

    /**
     * 학생 정보를 삭제한다 - 삭제된 데이터의 수가 리턴된다
     * @param input - 삭제할 조건을 담고 있는 Beans
     * @return int - 삭제된 데이터의 수
     * @throws Exception - SQL 처리에 실패한 경우
     */
    public int deleteItem(Student input) throws Exception;

    /**
     * 학생 정보를 조회한다. 조회된 데이터가 없는 경우 예외 발생.
     * @param input - 조회할 학생의 일련번호를 담고 있는 Beans
     * @return Student - 조회된 데이터
     * @throws Exception - SQL 처리에 실패한 경우
     */
    public Student getItem(Student input) throws Exception;

    /**
     * 학생 정보를 조회한다. 조회된 데이터가 없는 경우 예외 발생.
     * @param input
     * @return Student
     * @throws Exception - SQL 처리에 실패한 경우
     */
    public List<Student> getList(Student input) throws Exception;

    /**
     * 학생 목록에 대한 카운트 결과 반환
     * @param input - 조회할 학생의 학생번호를 담고 있는 Beans
     * @return Student - 조회된 데이터
     * @throws Exception - SQL 처리에 실패한 경우
     */
    public int getCount(Student input) throws Exception;
}
