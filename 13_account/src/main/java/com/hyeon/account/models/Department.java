package com.hyeon.crud.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/** Department 테이블의 구조를 정의하는 클래스 */
@Data
public class Department {
    private int deptNo;
    private String dname;
    private String loc;

    /**
     * 한 페이지에 표시될 목록 수
     * MySQL의 Limit 절에서 사용할 값이므로 Benas에 추가한다
     * 
     * 1) static 변수로 선언하여 모든 객체가 공유한다
     * 2) static 변수는 객체를 생성하지 않고도 사용할 수 있다
     * 3) static 변수에 lombok 을 적용하려면 @Getter, @Setter 개별적으로 적용한다
     */
    @Getter
    @Setter
    private static int listCount = 0;

    /**
     * MySQL의 Limit절에 사용될 offset 값
     * MySQL의 Limit절에서 사용할 값이므로 Beans에 추가한다
     * 
     * offset위치부터 listCount 만큼의 데이터를 가져온다
     */
    @Getter
    @Setter
    private static int offset = 0;

}
