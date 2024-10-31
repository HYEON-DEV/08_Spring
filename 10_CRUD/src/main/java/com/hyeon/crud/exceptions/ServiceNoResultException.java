package com.hyeon.crud.exceptions;
/**
 * ServiceNoResultException에 부합하지 않음을 의미하는 예외 상황을 처리
 */
public class ServiceNoResultException extends Exception {
    public ServiceNoResultException(String message) {
        super(message);
    }
}
