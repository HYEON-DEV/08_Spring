package com.hyeon.mailer.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MailController {
    
    @Autowired
    JavaMailSender javaMailSender;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/sendmail")
    public String postMethodName(
        @RequestParam("sender-name") String senderName,
        @RequestParam("sender-email") String senderEmail,
        @RequestParam("receiver-name") String receiverName,
        @RequestParam("receiver-email") String receiverEmail,
        @RequestParam("subject") String subject,
        @RequestParam("content") String content,
        HttpServletResponse response){

        

        /** 1) 메일 발송 정보 로그 확인 */
        log.debug("------------------------------");
        log.debug(String.format("SenderName : %s", senderName));
        log.debug(String.format("SenderEmail : %s", senderEmail));
        log.debug(String.format("ReceiverName : %s", receiverName));
        log.debug(String.format("ReceiverEmail : %s", receiverEmail));
        log.debug(String.format("Subject : %s", subject));
        log.debug(String.format("Content : %s", content));
        log.debug("------------------------------");

        /** 2) Java Mail 라이브러리 활용한 메일 발송
         * => import jakarta.mail.internet.MimeMessage;
         */
        MimeMessage message = javaMailSender.createMimeMessage();
        // => import org.springframework.mail.javamail.MimeMessageHelper;
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setTo(new InternetAddress(receiverEmail, receiverName, "UTF-8"));
            helper.setFrom(new InternetAddress(senderEmail, senderName, "UTF-8"));
        } catch (MessagingException e) {
            // 에러가 발생했음을 사용자에게 alert로 알리고 전 페이지로 이동하는 처리 필요
            log.error("메일 전송 실패", e);
            response.setContentType("text/html; charset=utf-8");
            try {
                PrintWriter out = response.getWriter();
                out.println("<script>");
                out.println("alert('" + e.getMessage() + "')");
                out.println("history.back()");
                out.println("</script>");
                out.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            // 에러가 발생했음을 사용자에게 alert로 알리고 전 페이지로 이동하는 처리 필요
            log.error("문자열 인코딩 실패", e);
            response.setContentType("text/html; charset=utf-8");
            try {
                PrintWriter out = response.getWriter();
                out.println("<script>");
                out.println("alert('" + e.getMessage() + "')");
                out.println("history.back()");
                out.println("</script>");
                out.flush();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //e.printStackTrace();
        }

        // 메일 보내기
        javaMailSender.send(message);

        return "redirect:/";
    }
}
