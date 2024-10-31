package com.hyeon.crud.services;

import java.util.List;

import com.hyeon.crud.exceptions.ServiceNoResultException;
import com.hyeon.crud.models.Department;

/**
 * 학과 관리 기능과 관련된 MyBatis Mappper를 간접적으로 호출하기 위한 기능 명세
 * 하나의 처리르 위해 두 개 이상의 Mapper 기능을 호출할 필요가 있을 경우,
 * 이 인터페이스의 구현체(Impl)을 통해서 처리한다
 * 
 * 1) delete 기능의 경우 삭제된 데이터 수를 의미하는 int 리턴
 * 2) 목록 조회 기능의 List<DTO클래스> 리턴
 * 3) 입력, 수정, 상세조회의 경우 DTO 클래스 리턴
 * 4) 모든 메서드는 throws exception 으로 예외발생할 경우 호출한 쪽에서 처리하도록 유도한다
 */
public interface DepartmentService {
    /**
     * 학과 정보를 새로 저장하고 저장된 정보를 조회하여 리턴한다
     * @param input - 저장할 정보를 담고 있는 Beans
     * @return Department - 저장된 데이터
     * @throws ServiceNoResultException - 저장된 데이터가 없는 경우
     */
    public Department addItem(Department input) throws ServiceNoResultException, Exception;

    /**
     * 학과 정보를 수정하고 수정된 정보를 조회하여 리턴한다
     * @param input - 수정할 정보를 담고 있는 Beans
     * @return Department - 수정된 데이터
     * @throws ServiceNoResultException
     */
    public Department editItem(Department input) throws ServiceNoResultException, Exception;

    /**
     * 학과 정보를 삭제한다 - 삭제된 데이터의 수가 리턴된다
     * @param input - 삭제할 조건을 담고 있는 Beans
     * @return int - 삭제된 데이터의 수
     * @throws ServiceNoResultException - 삭제된 데이터가 없는 경우
     */
    public int deleteItem(Department input) throws ServiceNoResultException, Exception;

    /**
     * 학과 정보를 조회한다. 조회된 데이터가 없는 경우 예외 발생.
     * @param input - 조회할 학과의 일련번호를 담고 있는 Beans
     * @return Department - 조회된 데이터
     * @throws ServiceNoResultException - 조회된 데이터가 없는 경우
     */
    public Department getItem(Department input) throws ServiceNoResultException, Exception;

    /**
     * 학과 정보를 조회한다. 조회된 데이터가 없는 경우 예외 발생.
     * @param input
     * @return Department
     * @throws ServiceNoResultException
     */
    public List<Department> getList(Department input) throws ServiceNoResultException, Exception;

    /**
     * 학과 목록에 대한 카운트 결과를 반환한다
     * @param input - 조회할 학과의 학과번호를 담고 있는 Beans
     * @return Department - 조회된 데이터
     * @throws Exception - SQL처리에 실패한 경우
     */
    public int getCount(Department input) throws Exception;
}
