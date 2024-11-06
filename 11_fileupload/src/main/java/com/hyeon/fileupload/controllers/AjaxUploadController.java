package com.hyeon.fileupload.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AjaxUploadController {
    
    @GetMapping("/api/upload")
    public String upload() {
        return "api/upload";
    }


    @GetMapping("/api/upload_multi")
    public String uploadMulti() {
        return "api/upload_multi";
    }
}
