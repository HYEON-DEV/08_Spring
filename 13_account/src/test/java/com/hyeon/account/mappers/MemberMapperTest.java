package com.hyeon.account.mappers;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hyeon.account.models.Member;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class MemberMapperTest {
    
    // 테스트 클래스에서는 객체 주입을 사용해야한다
    @Autowired
    private MemberMapper memberMapper;

    @Test
    @DisplayName("회원 추가 테스트")
    void insertMember() {
        Member input = new Member();
        input.setUserId("elly");
        input.setUserPw("1234qwer");
        input.setUserName("엘리");
        input.setEmail("ernandes@gmail.com");
        input.setPhone("010-6666-5678");
        input.setBirthday("19960101");
        input.setGender("M");
        input.setPostcode("12345");
        input.setAddr1("서울특별시");
        input.setAddr2("잠실동");
        
        int output = memberMapper.insert(input);
        
        log.debug("output: " + output);
        log.debug("new id: " + input.getId());
    }
 
    @Test
    @DisplayName("회원 수정 테스트")
    void updateMember() {
        Member input = new Member();
        input.setId(12);
        input.setUserId("hyn");
        input.setUserPw("1234qwer");
        input.setUserName("이름");
        input.setEmail("hyeon970315@gmail.com");
        input.setPhone("010-3333-5678");
        input.setBirthday("19930501");
        input.setGender("M");
        input.setPostcode("78907");
        input.setAddr1("서울특별시");
        input.setAddr2("잠실동");
        
        int output = memberMapper.update(input);
        
        log.debug("output: " + output);
    }

    @Test
    @DisplayName("회원 삭제 테스트")
    void deleteMember() {
        Member input = new Member();
        input.setId(3);

        int output = memberMapper.delete(input);
        
        log.debug("output: " + output);
    }

    @Test
    @DisplayName("하나의 회원 조회 테스트")
    void selectOneMember() {
        Member input = new Member();
        input.setId(1);

        Member output = memberMapper.selectItem(input);
        
        log.debug("output: " + output.toString());
    }

    @Test
    @DisplayName("회원 목록 조회 테스트")
    void selectListMember() {
        List<Member> output = memberMapper.selectList(null);

        for (Member item : output) {
            log.debug("output: " + item.toString());
        }
    }
     
    @Test
    @DisplayName("비밀번호 변경 테스트")
    void updatePw() {
        Member input = new Member();
        input.setUserPw("asd123");
        input.setUserId("hyeon");
        input.setEmail("qwe123@qwe.com");
        
        int output = memberMapper.resetPw(input);
        
        log.debug("output: " + output);
    }


    @Test
    @DisplayName("로그인 테스트")
    void login() {
        Member input = new Member();
        input.setUserId("elly");
        input.setUserPw("1234qwer");

        Member output = memberMapper.login(input);

        log.debug(("output : " + output.toString()));
    }
}
