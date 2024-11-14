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
    public Member editItem(Member input) throws Exception {
        int rows = 0;

        try {
            rows = memberMapper.update(input);

            if (rows == 0){
                throw new Exception("현재 비밀번호를 확인하세요");
            }
        } catch (Exception e ) {
            log.error("Member 데이터 수정에 실패했습니다", e);
            throw e;
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
    public void isUniqueEmail(Member input) throws Exception {
        //Member input = new Member();
        //input.setEmail(email);

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


    @Override
    public Member login(Member input) throws Exception {
        Member output = null;
        
        try {
            output = memberMapper.login(input);

            if (output == null) {
                throw new Exception("아이디 혹은 이메일을 확인하세요");
            }
        } catch (Exception e) {
            log.error("Member 데이터 조회에 실패했습니다", e);
            throw e;
        }

        // 데이터 조회에 성공했다면 마지막 로그인 시각 갱신
        try {
            int rows = memberMapper.updateLoginDate(output);
            
            if (rows == 0) {
                throw new Exception("존재하지 않는 회원에 대한 요청입니다");
            }
        } catch (Exception e) {
            log.error("Member 데이터 갱신에 실패했습니다", e);
        }

        return output;
    }


    /**
     * 회원 탈퇴 (업데이트)
     */
    @Override
    public int out(Member input) throws Exception {
        int rows = 0;

        try {
            rows = memberMapper.out(input);

            if (rows == 0) {
                throw new Exception("비밀번호가 맞지 않거나 존재하지 않는 회원에 대한 탈퇴 요청입니다.");
            }
        } catch (Exception e) {
            log.error("Member 데이터 수정에 실패했습니다", e);
            throw e;
        }
        
        return rows;
    }


    /**
     * 탈퇴하고 특정 시간이 지난 회원의 photo 조회, 회원 삭제
     */
    @Override
    public List<Member> processOutMembers() throws Exception {
        List<Member> output = null;

        try {
            // 1) is_out이 Y인 상태로 특정 시간이 지난 데이터를 조회한다
            output = memberMapper.selectOutMembersPhoto();
            // 2) 탈퇴 요청된 데이터 삭제
            memberMapper.deleteOutMembers();
        } catch(Exception e) {
            throw new Exception("탈퇴 처리에 실패했습니다.");
        }
        
        return output;
    }


    
    
    
}
