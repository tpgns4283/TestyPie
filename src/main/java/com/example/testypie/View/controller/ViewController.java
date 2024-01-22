package com.example.testypie.View.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping
public class ViewController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/home/login")
    public String loginSuccess() {
        return "home";
    }

    @GetMapping("/home/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/home/kakao-login/callback")
    public @ResponseBody String kakaoCallback(String code) {//Data를 리턴해주는 컨트롤러 함수

        return "카카오 인증 완료(코드값):" + code;
    }

    @GetMapping("/api/category/{parentName}/{childId}/products")
    public String addProduct(@PathVariable String parentName, @PathVariable Long childId, Model model) {
        model.addAttribute("parentName", parentName);
        model.addAttribute("childId", childId);
        return "addProductForm"; // 이동할 뷰의 이름을 반환
    }
}

