package com.example.testypie.View.controller;

import com.example.testypie.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import com.example.testypie.domain.user.kakao.service.KakaoService;
import com.example.testypie.global.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class ViewController {
    private final KakaoService kakaoService;
    private final JwtUtil jwtUtil;

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("LOGGED_IN_USER");
        // 사용자 정보가 있다면, 모델에 추가
        if (user != null) {
            model.addAttribute("account", user.getAccount());
        }
        return "home";
    }

    @GetMapping("/home/login")
    public String loginSuccess() {
        return "login";
    }

    @GetMapping("/home/signup")
    public String signup() {
        return "signup";
    }

    @GetMapping("/home/kakao-login/callback")
    @ResponseBody
    public String kakaoCallback(@RequestParam String code, HttpServletResponse res) throws JsonProcessingException {//Data를 리턴해주는 컨트롤러 함수
        String token = kakaoService.kakaoLogin(code);

        log.info("잘린 토큰: " + token.substring(7));

        // jwt토큰 만들기
//        res.setHeader(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));

        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.substring(7));
        cookie.setPath("/");
        res.addCookie(cookie);

        return "home";
    }

    @GetMapping("/api/category/{parentName}/{childId}/products")
    public String addProduct(@PathVariable String parentName, @PathVariable Long childId, Model model) {
        model.addAttribute("parentName", parentName);
        model.addAttribute("childId", childId);
        return "addProductForm"; // 이동할 뷰의 이름을 반환
    }

    @GetMapping("/api/category/{parentName}/{childId}/products/{productId}/update")
    public String updateProduct(@PathVariable String parentName, @PathVariable Long childId, Model model, @PathVariable Long productId) {
        model.addAttribute("parentName", parentName);
        model.addAttribute("childId", childId);
        model.addAttribute("productId", productId);
        return "updateProductForm"; // 이동할 뷰의 이름을 반환
    }

    @GetMapping("/api/users/{account}/update")
    public String updateProfile(@PathVariable String account,
                                           Model model) {

        model.addAttribute("account", account);
        return "updateProfileForm";
    }
}

