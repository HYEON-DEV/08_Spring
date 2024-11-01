package kr.hossam.crud.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Professor {
    private int profno; // 교수번호
    private String name; // 이름
    private String userid; // 아이디
    private String position; // 직급
    private int sal; // 급여
    private String hiredate; // 입사일
    private Integer comm; // 보직수당 --> Null 허용이므로 Integer로 선언
    private int deptno; // 부서번호(학과번호)
    private String dname; // 학과명 (조인을 통해 조회된 값)

    @Getter
    @Setter
    private static int listCount = 0;

    @Getter
    @Setter
    private static int offset = 0;
}