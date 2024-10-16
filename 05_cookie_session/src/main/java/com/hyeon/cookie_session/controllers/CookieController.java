package com.hyeon.cookie_session.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hyeon.cookie_session.helpers.UtilHelper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class CookieController {
    
    /** 쿠키 저장을 위한 작성 페이지 */
    @GetMapping("/cookie/home")
    public String home(Model model,
        @CookieValue(value="name", defaultValue="") String myCookieName,
        @CookieValue(value="age", defaultValue="0") int myCookieAge) {
        // import org.springframework.web.bind.annotation.CookieValue;
        
        try {
            // 저장시에 URLEncoding이 적용되었으므로 URLDecoding이 별도로 필요하다
            myCookieName = URLDecoder.decode(myCookieName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 추출한 값 View에 전달
        model.addAttribute("myCookieName", myCookieName);
        model.addAttribute("myCookieAge", myCookieAge);

        return "/cookie/home";
    }


    /** 쿠키를 저장하기 위한 action 페이지 */
    @PostMapping("/cookie/save")
    public String save(HttpServletResponse response, 
        @RequestParam(value = "cookie_name", defaultValue = "") String cookieName,
        @RequestParam(value = "cookie_time", defaultValue = "0") int cookieTime,
        @RequestParam(value = "cookie_var", defaultValue = "") String cookieVar) {
            
            /**
             *  1) 파라미터를 쿠키에 저장하기 위한 URLEncoding 처리
             * 쿠키 저장을 위해서 URLEncoding 처리가 필요하다
             */
            if (!cookieVar.equals("") ) {
                try {
                    // import java.net.URLEncoder;
                    cookieVar = URLEncoder.encode(cookieVar, "utf-8");
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
            Cookie cookie = new Cookie(cookieName, cookieVar);

            // 유효경로 => 사이트 전역에 대한 설정
            cookie.setPath("/");
            
            /**
             * 유효 도메인 (로컬 개발시에는 설정할 필요 X)
             * => "www.naver.com" 인 경우, ".naver.com"으로 설정 
             * cookie.setDomain("localhost");
             */

            // 유효시간 설정 ( 0이하면 즉시삭제, 초단위 )
            // 설정하지 않을 경우 브라우저 닫기 전까지 유지된다              
            cookie.setMaxAge(cookieTime);

            // 쿠키 저장
            response.addCookie(cookie);

            /**
             *  3) 강제 페이지 이동
             * 이 페이지에 머물렀다는 사실이 웹 브라우저의 history에 남지 않는다
             */
            return "redirect:/cookie/home";
        }


    /** 팝업창 제어 페이지 */
    @GetMapping("/cookie/popup")
    public String popup(Model model,
        @CookieValue(value="no-open", defaultValue ="") String noOpen) {
        
        //쿠키값 view에 전달
        model.addAttribute("no-open", noOpen);
        
        return "/cookie/popup";
    }

    @PostMapping("/cookie/popup_close")
    public String popupClose(HttpServletResponse response,
        @RequestParam(value="no-open", defaultValue="") String noOpen) {
        
        UtilHelper.getInstance().writeCookie(response, "no-open", noOpen, 60);

        return "redirect:/cookie/popup";
    }
}
