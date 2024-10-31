package com.hyeon.crud.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hyeon.crud.exceptions.ServiceNoResultException;
import com.hyeon.crud.mappers.MemberMapper;
import com.hyeon.crud.models.Member;
import com.hyeon.crud.services.MemberService;


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
    
}
