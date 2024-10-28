package com.hyeon.database.services;

import java.util.List;

import com.hyeon.database.models.Professor;


public interface ProfessorService {
    /**
     * 교수 정보를 새로 저장하고 저장된 정보를 조회하여 리턴한다
     * @param input - 저장할 정보를 담고 있는 Beans
     * @return Professor - 저장된 데이터
     * @throws Exception - SQL 처리에 실패한 경우
     */
    public Professor addItem(Professor input) throws Exception;

    /**
     * 교수 정보를 수정하고 수정된 정보를 조회하여 리턴한다
     * @param input - 수정할 정보를 담고 있는 Beans
     * @return Professor - 수정된 데이터
     * @throws Exception - SQL 처리에 실패한 경우
     */
    public Professor editItem(Professor input) throws Exception;

    /**
     * 교수 정보를 삭제한다 - 삭제된 데이터의 수가 리턴된다
     * @param input - 삭제할 조건을 담고 있는 Beans
     * @return int - 삭제된 데이터의 수
     * @throws Exception - SQL 처리에 실패한 경우
     */
    public int deleteItem(Professor input) throws Exception;

    /**
     * 교수 정보를 조회한다. 조회된 데이터가 없는 경우 예외 발생.
     * @param input - 조회할 교수의 일련번호를 담고 있는 Beans
     * @return Professor - 조회된 데이터
     * @throws Exception - SQL 처리에 실패한 경우
     */
    public Professor getItem(Professor input) throws Exception;

    /**
     * 교수 정보를 조회한다. 조회된 데이터가 없는 경우 예외 발생.
     * @param input - 조회할 교수의 교수번호를 담고 있는 Beans
     * @return Professor - 조회된 데이터
     * @throws Exception - SQL 처리에 실패한 경우
     */
    public List<Professor> getList(Professor input) throws Exception;

    /**
     * 학과 목록에 대한 카운트 결과 반환
     * @param input - 조회할 교수의 교수번호를 담고 있는 Beans
     * @return Professor - 조회된 데이터
     * @throws Exception - SQL 처리에 실패한 경우
     */
    public int getCount(Professor input) throws Exception;
}
