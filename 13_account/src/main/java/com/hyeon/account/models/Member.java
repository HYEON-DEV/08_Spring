package com.hyeon.account.models;

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


// 세션에 저장할 클래스 타입은 Serializable 인터페이스를 상속해야 한다
// Serializable : "객체직렬화"
@Data
public class Member implements Serializable {
    private int id;
    private String userId;
    private String userPw;
    private String userName;
    private String email;
    private String phone;
    private String birthday;
    private String gender;
    private String postcode;
    private String addr1;
    private String addr2;
    private String isOut;
    private String isAdmin;
    private String loginDate;
    private String regDate;
    private String editDate;
    private String photo;

    // 재로그인 필요
    private String newUserPw;   // 회원정보 수정에서 사용할 신규 비밀번호

    @Getter
    @Setter
    private static int listCount = 0;

    @Getter
    @Setter
    private static int offset = 0;
}
