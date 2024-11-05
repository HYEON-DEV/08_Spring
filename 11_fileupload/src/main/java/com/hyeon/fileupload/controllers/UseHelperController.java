package com.hyeon.fileupload.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hyeon.fileupload.helpers.FileHelper;
import com.hyeon.fileupload.helpers.WebHelper;
import com.hyeon.fileupload.models.UploadItem;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class UseHelperController {

    @Autowired
    private WebHelper webHelper;

    @Autowired
    private FileHelper fileHelper;


    
    @GetMapping("/use_helper/upload_single")
    public String uploadSingle () {
        return "use_helper/upload_single";
    }


    
    //@SuppressWarnings("null")
    @PostMapping("/use_helper/upload_single_ok")
    public String postMethodName(Model model,
        @RequestParam(value="profile-photo", required=false) MultipartFile profilePhoto) {

        UploadItem item = null;
        
        try {
            item = fileHelper.saveMultipartFile(profilePhoto);
        } catch (Exception e) {
            webHelper.serverError(e);
            return null;
        }
        
        // 업로드 정보를 View로 전달한다
        model.addAttribute("item", item);

        return "use_helper/upload_single_ok"; 
    }
        
}
