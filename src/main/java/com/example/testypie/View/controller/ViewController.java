package com.example.testypie.View.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping
public class ViewController {

    @GetMapping("/home")
    public String home() {
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
    public @ResponseBody String kakaoCallback(String code) {//Data를 리턴해주는 컨트롤러 함수
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        //key value 형태
        //HttpHeader 생성
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 카카오 인증에 필요한 데이터 map형식으로 넣기
        // 리펙토링 요소: 변수화 해서 사용하기
        // HttpBody 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "58c7ccd47e3b71e699a7c7d0c56341e5");
        params.add("redirect_uri", "http://localhost:8080/home/kakao-login/callback");
        params.add("code", code);

        // HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        // Http Post방식으로 요청하기
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        return "카카오 인증 완료(코드값):" + response;
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
}

