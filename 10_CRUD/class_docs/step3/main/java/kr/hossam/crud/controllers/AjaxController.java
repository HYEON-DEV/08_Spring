package kr.hossam.crud.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AjaxController {
    @GetMapping("/ajax/dept_item")
    public String deptItem() {
        return "/ajax/dept_item.html";
    }

    @GetMapping("/ajax/dept_list")
    public String deptList() {
        return "/ajax/dept_list.html";
    }

    @GetMapping("/ajax/dept_view")
    public String deptView() {
        return "/ajax/dept_view.html";
    }
}
