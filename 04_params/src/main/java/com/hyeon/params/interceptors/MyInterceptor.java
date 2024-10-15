package com.hyeon.params.interceptors;

import java.util.Map;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.hyeon.params.helpers.UtilHelper;

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //log.debug("MyInterceptor.preHandle 실행됨");

        // preHandle 접속됐다 => 어딘가 새로운 접속자가 컨트롤러에 붙었다는 것
        // 기존인지 신규인지는 모르지만 
        // 어떤 컨트롤러에 접속했다는 것
        log.info("---------- new client connect ----------");
        // => 새로운 사용자가 접속한 직후

        // 1) 페이지의 실행 시작 시각을 구한다
        startTime = System.currentTimeMillis();
        // new Date().getTime() 같은 기능
        // 사용자가 접속한 시각

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
        // GET방식인지, POST방식인지 조회
        String methodName = request.getMethod();
        // URL에서 "?"이후에 전달되는 GET파라미터 문자열을 모두 가져온다
        String queryString = request.getQueryString();
        // 가져온 값이 있다면 URL과 결합하여 완전한 URL을 구성한다
        if (queryString != null) {
            url += "?" + queryString;
        }
        // 획득한 정보를 로그로 표시한다
        log.info(String.format("[%s] %s", methodName, url));
        // 프로젝트하다보면 여러명이 같이 작업하니까 누가 테스트할 때 에러났는지 볼 수 있다

        // 4) 클라이언트가 전달한 모든 파라미터 확인하기
        Map<String, String[]> params = request.getParameterMap();

        for (String key : params.keySet()) {
            String[] value = params.get(key);
            log.info(String.format("(param) <-- %s = %s", key, String.join(",", value)));
        }
        
        // 5) 클라이언트가 머물렀던 이전 페이지 확인하기
        String referer = request.getHeader("referer");
        // 웹브라우저에 주소를 직접 입력하거나, 브라우저가 처음으로 떴을 때는 값이 없다(null)
        // a->b 링크 클릭하면, b접속시 referer값이 a 로 뜬다

        // 이전에 머물렀던 페이지가 존재한다면
        // -> 직전 종료시간과 이번 접속의 시작시간 과의 차이는 이전 페이지에 머문 시간을 의미한다
        if (referer != null && endTime > 0) {
            log.info(String.format("- REFERER : time=%d, url=%s", startTime-endTime, referer));
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }


    // preHandle() -> Controller -> postHandle() 가 실행돼야 한 페이지의 실행이 온전히 끝난 것


    /**
     * view 단으로 forward 되기 전에 수행된다
     * 컨트롤러 로직이 실행된 이후 호출된다
     * 예) return "myview"; => return 직전에 실행된다
     * 컨트롤러 단에서 에러 발생 시 헤당 메서드는 수행되지 않는다
     * request로 넘어온 데이터 가공시 많이 사용된다
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse
    response, Object handler,@Nullable ModelAndView modelAndView) throws Exception {
        //log.debug("MyInterceptor.postHandle 실행됨");

        // 1) 컨트롤러의 실행 종료 시각
        endTime = System.currentTimeMillis();

        // 2) 컨트롤러가 실행하는데 걸린 시각을 구한다
        log.info(String.format("running time : %d(ms)", endTime-startTime));

        HandlerInterceptor.super.postHandle(request, response, handler,
        modelAndView);
    }


    // 모든 컨트롤러의 실행시간은 1초안에 끝나야 한다 (안그럼 너무 느린 것)
    // 넘어간다면 sql 잘못된게 없는지 확인필요, 튜닝작업


    /**
     * 컨트롤러 종료 후 view가 정상적으로 렌더링된 후 제일 마지막에 실행되는 메서드
     * (잘 사용 안함)
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        //log.debug("MyInterceptor.afterCompletion 실행됨");
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
