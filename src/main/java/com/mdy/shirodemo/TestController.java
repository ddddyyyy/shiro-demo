package com.mdy.shirodemo;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.apache.shiro.subject.Subject;

@RestController
public class TestController {

    @PostMapping("login")
    public void login(){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername("test");
        token.setPassword("test".toCharArray());
        subject.login(token);
    }

    @GetMapping("test")
    public void test(){
        System.out.println(SecurityUtils.getSubject().getPrincipal());
    }
}
