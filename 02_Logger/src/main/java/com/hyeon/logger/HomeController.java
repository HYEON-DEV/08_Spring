package com.hyeon.logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;

@Slf4j
@Controller
public class HomeController {
    /**
     * 
     * @param model
     * @param request - HttpServletRequest 객체 (브라우저로부터의 요청 정보)
     * @return View 이름
     */
    @GetMapping("/")
    public String helloworld(Model model, HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
            log.info(">>>> Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP"); // 웹로직
            log.info(">>>> WL-Proxy-Client-IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            log.info(">>>> HTTP_CLIENT_IP : " + ip);
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            log.info(">>>> HTTP_X_FORWARDED_FOR : " + ip);
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        model.addAttribute("clientIp", ip);
        log.debug(">>>> Client IP : " + ip);


        /** 접근한 웹 브라우저의 UserAgent값 얻기 */
        String ua = request.getHeader("user-agent");
        model.addAttribute("ua", ua);
        log.debug(">>>> User-Agent : " + ua);
        
        /** UserAgent값을 파싱하여 브라우저 정보 얻기 */
        // --> import ua_parser.Client;
        Parser uaParser = new Parser();
        // --> import ua_parser.Parser;
        Client c = uaParser.parse(ua);

        model.addAttribute("uac", c.toString());
        log.debug(">>>> User-Agent : " + c.toString());

        // 브라우저 정보를 Model 객체에 추가
        model.addAttribute("browserFamily", c.userAgent.family);
        model.addAttribute("browserMajor", c.userAgent.major);
        model.addAttribute("browserMinor", c.userAgent.minor);

        log.debug("browserFamily : " + c.userAgent.family);
        log.debug("browserMajor : " + c.userAgent.major); 
        log.debug("browserMinor : " + c.userAgent.minor); 

        // OS 정보를 Model 객체에 추가
        model.addAttribute("osFamily", c.os.family);
        model.addAttribute("osMajor", c.os.major);
        model.addAttribute("osMinor", c.os.minor);

        log.debug("osFamily : " + c.os.family); 
        log.debug("osMajor : " + c.os.major); 
        log.debug("osMinor : " + c.os.minor); 

        // 디바이스 정보를 Model 객체에 추가
        model.addAttribute("deviceFamily", c.device.family);
        log.debug("deviceFamily : " + c.device.family); 

        // 웹브라우저가 컨트롤러에 전달한 요청(URL) 확인
        // 현재 URL 획득
        String url = request.getRequestURL().toString();

        // 접속 방식 조회
        String methodName = request.getMethod();

        // URL에서 "?"이후에 전달되는 GET 파라미터 문자열 모두 가져온다
        String queryString = request.getQueryString();

        // 가져온 값이 있다면 URL 과 결합하여 완전한 URL 구성
        if (queryString != null) {
            url += "?" + queryString;
        }

        model.addAttribute("method", methodName);
        model.addAttribute("url", url);

        return "index";
    }
}
