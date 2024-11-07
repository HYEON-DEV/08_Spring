package com.hyeon.account.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hyeon.account.helpers.RestHelper;
import com.hyeon.account.services.MemberService;


@RestController
public class AccountRestController {
    
    @Autowired
    private RestHelper restHelper;

    @Autowired
    private MemberService memberService;

    
    @GetMapping("/api/account/id_unique_check")
    public Map<String,Object> idUniqueCheck(@RequestParam("user_id") String userId) {
        try {
            memberService.isUniqueUserId(userId);
        } catch (Exception e) {
            return restHelper.badRequest(e);
        }

        return restHelper.sendJson();
    }


    @GetMapping("/api/account/email_unique_check")
    public Map<String,Object> emailUniqueCheck(@RequestParam("email") String email) {
        try {
            memberService.isUniqueEmail(email);
        } catch (Exception e) {
            return restHelper.badRequest(e);
        }

        return restHelper.sendJson();
    }
}
