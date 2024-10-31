package com.hyeon.restfulapi.controllers;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyeon.restfulapi.helpers.RestHelper;

import lombok.extern.slf4j.Slf4j;


/**
 * RESTful API 방식의 컨트롤러
 * X,Y의 사칙연산 수행
 * X,Y는 모두 0보다 커야 한다
 */
@Slf4j
@RestController
public class CalcRestfulController {

    @Autowired
    private RestHelper restHelper;

    
    @GetMapping("/my_calc")
    public Map<String, Object> plus( 
        @RequestParam(value="x", defaultValue="0") int x, 
        @RequestParam(value="y", defaultValue="0") int y ) {

        /** 1) 파라미터 유효성 검사 */

        if (x<0) {
            // case 1) 에러 내용을 직접 문자열로 전달
            return restHelper.badRequest("x는 0보다 커야 합니다");
        }

        if (y<0) {
            // case 2) 에러 내용을 예외 객체로 전달하는 경우
            // catch 블록에서 사용하는 경우 가정
            NumberFormatException e = new NumberFormatException("y는 0보다 커야 한다");
            return restHelper.badRequest(e);
        }


        /** 2) 처리해야 할 로직 수행 (DB 연동 등 가정) */
        int result = 0;

        try {
            result = x + y;
        } catch (Exception e) {
            return restHelper.serverError(e);
        }
        
        
        /** 3) 응답 결과 구성 */
        Map<String, Object> output = new LinkedHashMap<String, Object>();

        output.put("x", x);
        output.put("y", y);
        output.put("result", result);

        return output;
    }


    @PostMapping("/my_calc")
    public Map<String, Object> minus( 
        @RequestParam(value="x", defaultValue="0") int x, 
        @RequestParam(value="y", defaultValue="0") int y ) {

        /** 1) 파라미터 유효성 검사 */

        if (x<0) {
            // case 1) 에러 내용을 직접 문자열로 전달
            return restHelper.badRequest("x는 0보다 커야 합니다");
        }

        if (y<0) {
            // case 2) 에러 내용을 예외 객체로 전달하는 경우
            // catch 블록에서 사용하는 경우 가정
            NumberFormatException e = new NumberFormatException("y는 0보다 커야 한다");
            return restHelper.badRequest(e);
        }


        /** 2) 처리해야 할 로직 수행 (DB 연동 등 가정) */
        int result = 0;

        try {
            result = x - y;
        } catch (Exception e) {
            return restHelper.serverError(e);
        }
        
        
        /** 3) 응답 결과 구성 */
        Map<String, Object> output = new LinkedHashMap<String, Object>();

        output.put("x", x);
        output.put("y", y);
        output.put("result", result);

        return output;
    }


    @PutMapping("/my_calc")
    public Map<String, Object> times( 
        @RequestParam(value="x", defaultValue="0") int x, 
        @RequestParam(value="y", defaultValue="0") int y ) {

        /** 1) 파라미터 유효성 검사 */

        if (x<0) {
            // case 1) 에러 내용을 직접 문자열로 전달
            return restHelper.badRequest("x는 0보다 커야 합니다");
        }

        if (y<0) {
            // case 2) 에러 내용을 예외 객체로 전달하는 경우
            // catch 블록에서 사용하는 경우 가정
            NumberFormatException e = new NumberFormatException("y는 0보다 커야 한다");
            return restHelper.badRequest(e);
        }


        /** 2) 처리해야 할 로직 수행 (DB 연동 등 가정) */
        int result = 0;

        try {
            result = x * y;
        } catch (Exception e) {
            return restHelper.serverError(e);
        }
        
        
        /** 3) 응답 결과 구성 */
        Map<String, Object> output = new LinkedHashMap<String, Object>();

        output.put("x", x);
        output.put("y", y);
        output.put("result", result);

        return output;
    }

    @DeleteMapping("/my_calc")
    public Map<String, Object> divide( 
        @RequestParam(value="x", defaultValue="0") int x, 
        @RequestParam(value="y", defaultValue="0") int y ) {

        /** 1) 파라미터 유효성 검사 */

        if (x<0) {
            // case 1) 에러 내용을 직접 문자열로 전달
            return restHelper.badRequest("x는 0보다 커야 합니다");
        }

        if (y<1) {
            // case 2) 에러 내용을 예외 객체로 전달하는 경우
            // catch 블록에서 사용하는 경우 가정
            NumberFormatException e = new NumberFormatException("y는 0보다 커야 한다");
            return restHelper.badRequest(e);
        }


        /** 2) 처리해야 할 로직 수행 (DB 연동 등 가정) */
        int result = 0;

        

        try {
            result = x/y;
        } catch (Exception e) {
            return restHelper.serverError(e);
        }
        
        
        /** 3) 응답 결과 구성 */
        Map<String, Object> output = new LinkedHashMap<String, Object>();

        output.put("x", x);
        output.put("y", y);
        output.put("result", result);

        return output;
    }
}
