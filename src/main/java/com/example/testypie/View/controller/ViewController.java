package com.example.testypie.View.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/home")
public class ViewController {

    @GetMapping
    public String index() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @ResponseBody
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/kakao-login/callback")
    public @ResponseBody String kakaoCallback(String code) {//Data를 리턴해주는 컨트롤러 함수

        return "카카오 인증 완료(코드값):" + code;
    }
}
