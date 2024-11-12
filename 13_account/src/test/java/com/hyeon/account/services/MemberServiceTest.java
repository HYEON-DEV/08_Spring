package com.hyeon.account.services;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hyeon.account.exceptions.ServiceNoResultException;
import com.hyeon.account.models.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MemberServiceTest {
    
    @Autowired
    private MemberService memberService;


    @Test
    @DisplayName("회원 추가 테스트")
    void insertMember() throws Exception {
        Member input = new Member();
        input.setUserId("lgtwins");
        input.setUserPw("1234qwer");
        input.setUserName("오지환");
        input.setEmail("lg@lgtwins.com");
        input.setPhone("010-6666-5678");
        input.setBirthday("19960101");
        input.setGender("M");
        input.setPostcode("12345");
        input.setAddr1("서울특별시");
        input.setAddr2("잠실동");

        Member output = null;

        try {
            output = memberService.addItem(input);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }

        if (output != null) {
            log.debug("output : " + output.toString());
        }
    }


    @Test
    @DisplayName("회원 수정 테스트")
    void updateMember() throws Exception {
        Member input = new Member();
        input.setId(1);
        input.setUserId("chann1111");
        input.setUserPw("1234qwer");
        input.setUserName("임찬규");
        input.setEmail("chan123@gmail.com");
        input.setPhone("010-3333-5678");
        input.setBirthday("19930501");
        input.setGender("M");
        input.setPostcode("78907");
        input.setAddr1("서울특별시");
        input.setAddr2("잠실동");

        Member output = null;

        try {
            output = memberService.editItem(input);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }

        if (output != null) {
            log.debug("output : " + output.toString());
        }
    }
 

    @Test
    @DisplayName("회원 삭제 테스트")
    void deleteMember() throws Exception {
        Member input = new Member();
        input.setId(4);
        
        try {
            memberService.deleteItem(input);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }
    }


    @Test
    @DisplayName("단일행 회원 조회 테스트")
    void selectOneMember() throws Exception {
        Member input = new Member();
        input.setId(1);
    
        Member output = null;

        try {
            output = memberService.getItem(input);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }

        if (output != null) {
            log.debug("output : " + output.toString());
        }
    }


    @Test
    @DisplayName("회원 목록 조회 테스트")
    void selectListMember() throws Exception {
        List<Member> output = null;

        try {
            output = memberService.getList(null);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }

        if (output != null) {
            for(Member item : output) {
                log.debug("output : " + item.toString());
            }
        }
  
    }


    @Test
    @DisplayName("로그인 테스트")
    void login() throws Exception {
        Member input = new Member();
        input.setUserId("elly");
        input.setUserPw("1234qwer");

        Member output = null;

        try {
            output = memberService.login(input);
        } catch (ServiceNoResultException e) {
            log.error("---------- SQL문 처리 결과 없음 ----------", e);
        }catch (Exception e) {
            log.error("---------- Mapper 구현 에러 ----------", e);
            throw e;
        }

        if (output != null) {
            log.debug("output : " + output.toString());
        }
    }
 
}
