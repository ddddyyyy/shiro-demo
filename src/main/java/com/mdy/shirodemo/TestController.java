package com.mdy.shirodemo;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("login")
    public void login() {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername("test");
        token.setPassword("test".toCharArray());
        token.setRememberMe(true);
        subject.login(token);
    }

    @GetMapping("test")
    public void test() {
        System.out.println(SecurityUtils.getSubject().getPrincipal());
    }
}
