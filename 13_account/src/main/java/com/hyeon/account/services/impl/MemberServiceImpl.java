package com.hyeon.account.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyeon.account.exceptions.ServiceNoResultException;
import com.hyeon.account.mappers.MemberMapper;
import com.hyeon.account.models.Member;
import com.hyeon.account.services.MemberService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MemberServiceImpl implements MemberService {

    /** SQL문 구현하고 있는 Mapper 객체 주입 */
    @Autowired
    private MemberMapper memberMapper;


    @Override
    public Member addItem(Member input) throws ServiceNoResultException, Exception {
        int rows = memberMapper.insert(input);

        if (rows == 0){
            throw new ServiceNoResultException("저장된 데이터가 없습니다");
        }
        
        return memberMapper.selectItem(input);
    }


    @Override
    public Member editItem(Member input) throws ServiceNoResultException, Exception {
        int rows = memberMapper.update(input);

        if (rows == 0){
            throw new ServiceNoResultException("수정된 데이터가 없습니다");
        }
        
        return memberMapper.selectItem(input);
    }


    @Override
    public int deleteItem(Member input) throws ServiceNoResultException, Exception {
        
        int rows = memberMapper.delete(input);

        if (rows==0) {
            throw new ServiceNoResultException("삭제된 데이터가 없습니다");
        }

        return rows;
    }


    @Override
    public Member getItem(Member input) throws ServiceNoResultException, Exception {
        Member output = memberMapper.selectItem(input);

        if (output==null) {
            throw new ServiceNoResultException("조회된 데이터가 없습니다");
        }

        return output;
    }

    
    @Override
    public List<Member> getList(Member input) throws ServiceNoResultException, Exception {
        return memberMapper.selectList(input);
    }


    @Override
    public void isUniqueUserId(String userId) throws Exception {
        Member input = new Member();
        input.setUserId(userId);

        int output = 0;

        try {
            output = memberMapper.selectCount(input);

            if (output > 0) {
                throw new Exception("사용할 수 없는 아이디 입니다.");
            }
        } catch (Exception e) {
            log.error("아이디 중복검사에 실패했습니다.", e);
            throw e;
        }
        
    }


    @Override
    public void isUniqueEmail(String email) throws Exception {
        Member input = new Member();
        input.setEmail(email);

        int output = 0;

        try {
            output = memberMapper.selectCount(input);

            if (output > 0) {
                throw new Exception("사용할 수 없는 이메일 입니다.");
            }
        } catch (Exception e) {
            log.error("아이디 중복검사에 실패했습니다.", e);
            throw e;
        }
    }


    @Override
    public Member findId(Member input) throws Exception {
        Member output = null;

        try {
            output = memberMapper.findId(input);

            if (output == null) {
                throw new Exception("Member 조회된 데이터가 없습니다");
            }
        } catch (Exception e) {
            log.error("===== 아이디 검색 실패 =====", e);
            throw e;
        }

        return output;
    }


    @Override
    public void resetPw(Member input) throws Exception {
        int rows = 0;

        try {
            rows = memberMapper.resetPw(input);

            if(rows==0) {
                throw new Exception("아이디와 이메일을 확인하세요.");
            }
        } catch (Exception e) {
            log.error("Member 데이터 수정에 실패했습니다.", e);
            throw e;
        }

    }
    
}
