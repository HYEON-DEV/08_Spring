package com.hyeon.interceptor.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hyeon.interceptor.helpers.UtilHelper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;

@Slf4j
@Component
@SuppressWarnings("null")
public class MyInterceptor implements HandlerInterceptor {
    // 페이지의 실행 시작 시각을 저장할 변수
    long startTime = 0;

    // 페이지의 실행 완료 시각을 저장할 변수
    long endTime = 0;

    /**
     * Controller 실행 전에 수행되는 메서드
     * 클라이언트(웹브라우저)의 요청을 컨트롤러에 전달 하기 전에 호출된다.
     * return 값으로 boolean 값을 전달하는데 false 인 경우
     * controller를 실행 시키지 않고 요청을 종료한다.
     * 보통 이곳에서 각종 체크작업과 로그를 기록하는 작업을 진행한다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse
        response, Object handler) throws Exception {
        //log.debug("MyInterceptor.preHandle 실행됨");
        log.info("---------- new client connect ----------");

        // 1) 페이지의 실행 시작 시각을 구한다
        startTime = System.currentTimeMillis();

        // 2) 접속한 클라이언트 정보 확인하기
        String ua = request.getHeader("user-agent");
        Parser uaParser = new Parser();
        Client c = uaParser.parse(ua);

        String fmt = "[Client] %s, %s, %s, %s, %s, %s";
        String ipAddr = UtilHelper.getInstance().getClientIp(request);
        String osVersion = c.os.major + (c.os.minor != null ? "."+c.os.minor : "");
        String uaVersion = c.userAgent.major + (c.userAgent.minor != null ? "."+c.userAgent.minor : "");
        String clientInfo = String.format(fmt, ipAddr, c.device.family, c.os.family, osVersion, c.userAgent.family, uaVersion);

        log.info(clientInfo);

        // 3) 클라이언트의 요청 정보 (URL) 확인하기
        // 현재 URL 획득
        String url = request.getRequestURL().toString();

        
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse
    response, Object handler,
    @Nullable ModelAndView modelAndView) throws Exception {
    log.debug("MyInterceptor.postHandle 실행됨");
    HandlerInterceptor.super.postHandle(request, response, handler,
    modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse
    response, Object handler,
    @Nullable Exception ex) throws Exception {
    log.debug("MyInterceptor.afterCompletion 실행됨");
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
