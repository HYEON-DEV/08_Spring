package com.hyeon.aop.services;

/**
 * 수행해야 하는 비즈니스 로직을 정의한 인터페이스
 */
public interface MyCalcService {
    
    /**
     * 두 개의 정수를 더하는 메서드
     * @param x - 첫 번째 정수
     * @param y - 두 번재 정수
     * @return int
     */
    public int plus(int x, int y);

    /**
     * 두 개의 정수를 빼는 메서드
     * @param x - 첫 번째 정수
     * @param y - 두 번재 정수
     * @return int
     */
    public int minus(int x, int y);
}
