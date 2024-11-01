package kr.hossam.crud.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Student {
    private int studno; // 학생번호
    private String name; // 이름
    private String userid; // 아이디
    private int grade; // 학년
    private String idnum; // 주민번호
    private String birthdate; // 생년월일
    private String tel; // 전화번호
    private int height; // 키
    private int weight; // 몸무게
    private int deptno; // 학과번호
    private Integer profno; // 담당교수의 일련번호

    private String dname; // 학과명 (조인을 통해 조회된 값)
    private String pname; // 교수명 (조인을 통해 조회된 값)

    @Getter
    @Setter
    private static int listCount = 0;

    @Getter
    @Setter
    private static int offset = 0;
}
