package com.iscas.workingdiary.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class WorkingDairyController {
    public void postWorkingDairyWithSign(HttpServletRequest request){
        String userName = request.getParameter("userName");
        String title = request.getParameter("title");
        String text = request.getParameter("text");

        
    }
}
