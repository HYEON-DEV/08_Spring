package com.hyeon.account.exceptions;

/**
 * 정규표현식에 부합하지 않음에 의미하는 예외 상황 처리
 */
public class StringFormatException extends Exception {
    public StringFormatException (String message) {
        super(message);
    }
}
