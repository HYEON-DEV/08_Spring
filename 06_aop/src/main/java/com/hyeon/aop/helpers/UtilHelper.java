package com.hyeon.aop.helpers;

import java.net.URLEncoder;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component  //  <- 스프링에게 이 클래스가 빈(Bean)임을 알려준다
public class UtilHelper {
    /* 싱글톤 객체
    private static UtilHelper current;

    public static UtilHelper getInstance() {
        if (current == null) {
            current = new UtilHelper();
        }

        return current;
    }

    public static void freeInstance() {
        current = null;
    }

    private UtilHelper() {
        super();
    }
    */


    /**
     * 랜덤 숫자를 생성하는 메서든
     * @param min - 최솟값
     * @param max - 최댓값
     * @return 생성된 랜덤 숫자
     */
    public int random(int min, int max) {
        int num = (int) ( ( Math.random() * (max-min+1) ) + min );
        return num;
    }
    
    /**
     * 클라이언트의 IP 주소 가져오는 메서드
     * @param request - HttpServletRequest 객체
     * @return IP 주소
     */
    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    
    /**
     * 쿠키값을 저장한다
     * @param response - HttpServletResponse 객체
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간 (0이면 지정 안함, 음수일 경우 즉시 삭제)
     * @param domain - 쿠키 도메인
     * @param path - 쿠키 경로
     */
    public void writeCookie(HttpServletResponse response, String name, String value, int maxAge, String domain, String path) {
        if ( value!=null && !value.equals("") ) {
            try {
                // import java.net.URLEncoder;
                value = URLEncoder.encode(value, "utf-8");
            } catch (UnsupportedEncodingException e) {
                // import java.io.UnsupportedEncodingException;
                e.printStackTrace();
            }
        }

        /**
         *  2) 쿠키 저장하기
         * 저장할 쿠키 객체 생성
         * import jakarta.servlet.http.Cookie;
         */
        Cookie cookie = new Cookie(name, value);

        // 유효경로 => 사이트 전역에 대한 설정
        cookie.setPath("/");
        
        /**
         * 유효 도메인 (로컬 개발시에는 설정할 필요 X)
         * => "www.naver.com" 인 경우, ".naver.com"으로 설정 
         * cookie.setDomain("localhost");
         */
        if (domain != null) {
            cookie.setDomain(domain);
        }

        // 유효시간 설정 ( 0이하면 즉시삭제, 초단위 )
        // 설정하지 않을 경우 브라우저 닫기 전까지 유지된다        
        if (maxAge != 0) {
            cookie.setMaxAge(maxAge);
        }

        // 쿠키 저장
        response.addCookie(cookie);
    }

    /**
     * 쿠키값을 저장한다. path값을 "/"로 강제 설정
     * @param response - HttpServletResponse 객체
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간 (0이면 지정 안함, 음수일 경우 즉시 삭제)
     * @param domain - 쿠키 도메인
     * 
     * @see #writeCookie(HttpServletResponse, String, String, int, String, String)
     */
    public void writeCookie(HttpServletResponse response, String name, String value, int maxAge, String domain) {
        this.writeCookie(response, name, value, maxAge, domain, "/");
    }

    /**
     * 쿠키값을 저장한다. path값을 "/"로, domain을 null로 강제 설정
     * @param response - HttpServletResponse 객체
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간 (0이면 지정 안함, 음수일 경우 즉시 삭제)
     * 
     * @see #writeCookie(HttpServletResponse, String, String, int, String, String)
     */
    public void writeCookie(HttpServletResponse response, String name, String value, int maxAge) {
        this.writeCookie(response, name, value, maxAge, null, "/");
    }

    /**
     * 쿠키값을 저장한다. path값을 "/"로, domain을 null로, maxAge를 0으로 강제 설정
     * @param response - HttpServletResponse 객체
     * @param name - 쿠키 이름
     * @param value - 쿠키 값
     * @param maxAge - 쿠키 유효 시간 (0이면 지정 안함, 음수일 경우 즉시 삭제)
     * 
     * @see #writeCookie(HttpServletResponse, String, String, int, String, String)
     */
    public void writeCookie(HttpServletResponse response, String name, String value) {
        this.writeCookie(response, name, value, 0, null, "/");
    }

    /** 
     * 쿠키값을 삭제한다
     * @param response - HttpServletResponse 객체
     * @param name - 쿠키 이름
     */
    public void deleteCookie(HttpServletResponse response, String name) {
        this.writeCookie(response, name, null, -1, null, "/");
    }
}
