package com.hyeon.crud.helpers;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class RestHelper {
    
    @Autowired
    private HttpServletResponse response;


    /**
     * JSON 형식의 응답을 출력하기 위한 메서드
     * @param status - HTTP 상태코드
     * @param message - 결과 메시지
     * @param data - JSON으로 변환할 데이터 컬렉션
     * @param error - 에러 메시지
     * @return Map<String, Object>
     */
    public Map<String, Object> sendJson (int status, String message, Map<String,Object> data, Exception error) {
        
        /** 1) JOSN 형식 출력을 위한 HTTP 헤더 설정 */

        // JSON 형식임을 명시
        response.setContentType("application/json; charset=UTF-8");
        
        // HTTP 상태 코드 설정
        response.setStatus(status);

        /* CORS 허용
         * CORS 보안 문제 방지 => CrossDomain 에 의한 접근 허용 
         * (보안에 좋지 않기 때문에 이 옵션을 허용할 경우 인증키 등의 보안장치가 필요하다)
         * 여기서는 실습을 위해 허용 */
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        response.setHeader("Access-Control-Allow-Origin", "*");


        /** 2) JSON으로 변환할 Map 객체 구성 */

        Map<String,Object> result = new LinkedHashMap<String,Object>();

        result.put("timestamp", LocalDateTime.now().toString());
        result.put("status", status);
        result.put("message", message);

        // data가 전달됐다면 result에 병합
        if (data != null) {
            result.putAll(data);
        }

        // error 있다면 result에 포함
        if (error != null) {
            result.put("error", error.getClass().getName());
            result.put("message", error.getMessage());

            // printStackTrace() 출력 내용 문자열로 반환받는다
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(bos);
            error.printStackTrace(ps);
            
            String trace = bos.toString();
            result.put("trace", trace);
        }

        return result;
    }



    /**  200
     * JSON 형식의 응답을 출력하기 위한 메서드
     * @param data - JSON으로 변환할 데이터 컬렉션
     * @return Map<String, Object>
     */
    public Map<String, Object> sendJson ( Map<String,Object> data ) {
        return this.sendJson(200, "OK", data, null);
    }



    /**  200
     * JSON 형식의 응답을 출력하기 위한 메서드
     * 특별한 결과값 없이 요청에 대한 성공 여부만을 알리기 위해 사용
     * @return Map<String, Object>
     */
    public Map<String, Object> sendJson ( ) {
        return this.sendJson(200, "OK", null, null);
    }


 
    /**
     * 에러 상황을 JSON 형식으로 출력하기 위한 메서드
     * @param status - HTTP 상태코드
     * @param message - 결과 메시지
     * @param error - 에러 이름
     * @return Map<String, Object>
     */
    public Map<String, Object> sendError (int status, String message) {
        Exception error = new Exception(message);
        return this.sendJson(status, null, null, error);
    }



    /**
     * JSON 형식으로 에러 메시지를 리턴한다
     * HTTP 상태코드 400으로 설정,  결과 메시지는 파라미터로 전달된 값을 설정한다
     * 파라미터 유효성 검사 실패 등의 경우에 사용한다
     * @param message - 결과 메시지
     * @return Map<String, Object>
     */
    public Map<String, Object> badRequest (String message) {
        return this.sendError(400, message);
    }



    /**
     * JSON 형식으로 에러 메시지를 리턴한다
     * HTTP 상태코드 400으로 설정,  결과 메시지는 파라미터로 전달되는 error 객체를 사용한다
     * 파라미터 유효성 검사 실패 등의 경우에 사용한다
     * @param error - 에러 객체
     * @return Map<String, Object>
     */
    public Map<String, Object> badRequest (Exception error) {
        return this.sendJson(400, null, null, error);
    }



    /**
     * JSON 형식으로 에러 메시지를 리턴한다
     * HTTP 상태코드 500으로 설정,  결과 메시지는 파라미터로 전달되는 값을 설정한다
     * 400 에러 이외의 모든 경우에 사용한다. 주로 DB 연동 등의 처리에서 발생한 에러를 처리한다
     * @param message - 에러 메시지
     * @return Map<String, Object>
     */
    public Map<String, Object> serverError (String message) {
        return this.sendError(500, message);
    }



    /**
     * JSON 형식으로 에러 메시지를 리턴한다
     * HTTP 상태코드 500으로 설정,  결과 메시지는 파라미터로 전달되는 error 객체를 사용한다
     * 400 에러 이외의 모든 경우에 사용한다. 주로 DB 연동 등의 처리에서 발생한 에러를 처리한다
     * @param error - 에러 객체
     * @return Map<String, Object>
     */
    public Map<String, Object> serverError (Exception error) {
        return this.sendJson(500, null, null, error);
    }
}
