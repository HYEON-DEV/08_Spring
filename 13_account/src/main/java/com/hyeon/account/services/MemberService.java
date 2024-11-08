package com.hyeon.account.services;

import java.util.List;

import com.hyeon.account.exceptions.ServiceNoResultException;
import com.hyeon.account.models.Member;


public interface MemberService {
    /**
     * 회원 정보를 새로 저장하고 저장된 정보를 조회하여 리턴한다
     * @param input - 저장할 정보를 담고 있는 Beans
     * @return Member - 저장된 데이터
     * @throws ServiceNoResultException - 저장된 데이터가 없는 경우
     */
    public Member addItem(Member input) throws ServiceNoResultException, Exception;

    /**
     * 회원 정보를 수정하고 수정된 정보를 조회하여 리턴한다
     * @param input - 수정할 정보를 담고 있는 Beans
     * @return Member - 수정된 데이터
     * @throws ServiceNoResultException
     */
    public Member editItem(Member input) throws ServiceNoResultException, Exception;

    /**
     * 회원 정보를 삭제한다 - 삭제된 데이터의 수가 리턴된다
     * @param input - 삭제할 조건을 담고 있는 Beans
     * @return int - 삭제된 데이터의 수
     * @throws ServiceNoResultException - 삭제된 데이터가 없는 경우
     */
    public int deleteItem(Member input) throws ServiceNoResultException, Exception;

    /**
     * 회원 정보를 조회한다. 조회된 데이터가 없는 경우 예외 발생.
     * @param input - 조회할 회원의 일련번호를 담고 있는 Beans
     * @return Member - 조회된 데이터
     * @throws ServiceNoResultException - 조회된 데이터가 없는 경우
     */
    public Member getItem(Member input) throws ServiceNoResultException, Exception;

    /**
     * 회원 정보를 조회한다. 조회된 데이터가 없는 경우 예외 발생.
     * @param input
     * @return Member
     * @throws ServiceNoResultException
     */
    public List<Member> getList(Member input) throws ServiceNoResultException, Exception;


    public void isUniqueUserId(String userId) throws Exception;

    
    public void isUniqueEmail(String email) throws Exception;

    
    public Member findId(Member input) throws Exception;
}
