package com.example.testypie.View.controller;

import static com.example.testypie.global.jwt.JwtUtil.REFRESH_AUTHORIZATION_HEADER;

import com.example.testypie.domain.feedback.dto.response.ReadFeedbackResponseDTO;
import com.example.testypie.domain.feedback.service.FeedbackService;
import com.example.testypie.domain.user.dto.response.ParticipatedProductResponseDTO;
import com.example.testypie.domain.user.dto.response.ProfileResponseDTO;
import com.example.testypie.domain.user.dto.response.RegisteredProductResponseDTO;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.kakao.service.KakaoService;
import com.example.testypie.domain.user.service.UserInfoService;
import com.example.testypie.global.jwt.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class ViewController {
  private final KakaoService kakaoService;
  private final UserInfoService userInfoService;
  private final FeedbackService feedbackService;
  private final JwtUtil jwtUtil;

  @GetMapping("/")
  public String home(Model model, HttpServletRequest request) {
    User user = (User) request.getSession().getAttribute("LOGGED_IN_USER");
    // 사용자 정보가 있다면, 모델에 추가
    if (user != null) {
      model.addAttribute("account", user.getAccount());
    }
    return "home";
  }

  @GetMapping("/login")
  public String loginSuccess() {
    return "login";
  }

  @GetMapping("/signup")
  public String signup() {
    return "signup";
  }

  // 카카오 로그인시 accessToken을 헤더에 refreshToken을 쿠키에 넣습니다.
  @GetMapping("/kakao-login/callback")
  public String kakaoCallback(@RequestParam String code, HttpServletResponse res, Model model)
      throws JsonProcessingException {
    // Data를 리턴해주는 컨트롤러 함수
    List<String> tokens = kakaoService.kakaoLogin(code); // 리프레시 저장

    String accessToken = tokens.get(0);
    String refreshToken = tokens.get(1);

    log.info(refreshToken);

    log.info("잘린 토큰: " + tokens.get(0).substring(7));

    // jwt토큰 access토큰 만들기
    model.addAttribute("token", accessToken);
    //        res.setHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken.substring(7));

    // token으로 리프레시 토큰만들어주기
    Cookie cookie = new Cookie(REFRESH_AUTHORIZATION_HEADER, refreshToken);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    res.addCookie(cookie);

    return "home";
  }

  @GetMapping("/api/category/{parentName}/{childId}/products")
  public String addProduct(
      @PathVariable String parentName, @PathVariable Long childId, Model model) {
    model.addAttribute("parentName", parentName);
    model.addAttribute("childId", childId);
    return "addProductForm"; // 이동할 뷰의 이름을 반환
  }

  @GetMapping("/api/category/{parentName}/{childId}/products/{productId}/update")
  public String updateProduct(
      @PathVariable String parentName,
      @PathVariable Long childId,
      Model model,
      @PathVariable Long productId) {
    model.addAttribute("parentName", parentName);
    model.addAttribute("childId", childId);
    model.addAttribute("productId", productId);
    return "updateProductForm"; // 이동할 뷰의 이름을 반환
  }

  @GetMapping("/api/users/{account}/update")
  public String updateProfile(@PathVariable String account, Model model) {

    model.addAttribute("account", account);
    return "updateProfileForm";
  }

  @GetMapping(
      "/api/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/surveys")
  public String addSurvey(
      @PathVariable Long product_id,
      @PathVariable Long childCategory_id,
      @PathVariable String parentCategory_name,
      Model model) {
    model.addAttribute("parentName", parentCategory_name);
    model.addAttribute("childId", childCategory_id);
    model.addAttribute("productId", product_id);
    return "addSurvey"; // 이동할 뷰의 이름을 반환
  }

  @GetMapping(
      "/api/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/feedback")
  public String addFeedback(
      @PathVariable Long product_id,
      @PathVariable Long childCategory_id,
      @PathVariable String parentCategory_name,
      Model model) {
    model.addAttribute("parentName", parentCategory_name);
    model.addAttribute("childId", childCategory_id);
    model.addAttribute("productId", product_id);
    return "addFeedback"; // 이동할 뷰의 이름을 반환
  }

  @GetMapping("/api/user/{account}")
  public ResponseEntity<ProfileResponseDTO> getProfile(@PathVariable String account, Model model) {
    User user = userInfoService.findProfile(account);
    ProfileResponseDTO res = ProfileResponseDTO.of(user);
    return ResponseEntity.ok(res);
  }

  @GetMapping(
      "/api/category/{parentCategory_name}/{childCategory_id}/products/{product_id}/bugreports")
  public String addBugReports(
      @PathVariable Long product_id,
      @PathVariable Long childCategory_id,
      @PathVariable String parentCategory_name,
      Model model) {
    model.addAttribute("parentName", parentCategory_name);
    model.addAttribute("childId", childCategory_id);
    model.addAttribute("productId", product_id);
    return "bugreport"; // 이동할 뷰의 이름을 반환
  }

    @GetMapping("/api/users/{account}/myProducts")
    public String getMyProducts(@PathVariable String account, Model model) {
        List<RegisteredProductResponseDTO> myProducts = userInfoService.getUserProducts(account);
        model.addAttribute("account", account);
        model.addAttribute("myProducts", myProducts);
        return "profileRegisteredProducts";
    }

    @GetMapping("/api/users/{account}/joinProducts")
    public String getParticipatedProducts(@PathVariable String account, Model model) {
        List<ParticipatedProductResponseDTO> participatedProducts =
                userInfoService.getUserParticipatedProducts(account);
        model.addAttribute("account", account);
        model.addAttribute("participatedProducts", participatedProducts);
        return "profileParticipatedProducts";
    }

    @GetMapping("/api/users/{account}/myProducts/{productId}/feedbacks")
    public String getFeedbacksFromProductId(
            @PathVariable String account, @PathVariable Long productId, Model model) {
        List<ReadFeedbackResponseDTO> feedbackList =
                feedbackService.getAllFeedbacksByProductId(productId).stream()
                        .map(ReadFeedbackResponseDTO::of)
                        .toList();
        model.addAttribute("account", account);
        model.addAttribute("feedbacks", feedbackList);
        return "profileViewFeedbacks";
    }

    @GetMapping("/api/users/{account}/averageRate")
    public String showAverageRatingPage(@PathVariable String account, Model model) {
      // 여기서 필요한 작업 수행 (평균 점수 계산 등)
      double averageRating = userInfoService.getAverageRating(account);

      // 모델에 평균 점수를 추가하여 Thymeleaf에서 사용할 수 있도록 함
      model.addAttribute("averageRating", averageRating);

      // 해당하는 Thymeleaf 템플릿을 반환 (평균 점수를 표시하는 페이지)
      return "profileAverageRating";
    }
}
