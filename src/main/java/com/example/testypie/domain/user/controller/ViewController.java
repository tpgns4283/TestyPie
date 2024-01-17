package com.example.testypie.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewController {
    @GetMapping("/loginForm")
    public String login() {
        return "login";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/api/users/kakao-login/callback")
    public @ResponseBody String kakaoCallback(String code) {//Data를 리턴해주는 컨트롤러 함수

        return "카카오 인증 완료(코드값):" + code;
    }

    @GetMapping("/signupForm")
    @ResponseBody
    public String signup() {
        return "signup";
    }
}
