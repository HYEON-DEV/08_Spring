package com.hyeon.aop.services.impl;

import org.springframework.stereotype.Service;

import com.hyeon.aop.services.MyCalcService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service    // <- 비즈니스 로직을 구현하는 모든 구현체 클래스에 명시
public class MyCalcServiceImpl implements MyCalcService {

    /** 
     * 이 객체가 생성되었음을 확인하기 위해 생성자 정의
     * 보통의 Service 구현체는 생성자를 정의하지 않는다
     */
    public MyCalcServiceImpl() {
        log.debug("MyCalcServiceImpl() 생성자 호출");
    }

    @Override
    public int plus(int x, int y) {
        return x+y;
    }

    @Override
    public int minus(int x, int y) {
        return x-y;
    }
    
}