package com.example.testypie.domain.reward.controller;

import com.example.testypie.domain.reward.dto.RewardCreateRequestDTO;
import com.example.testypie.domain.reward.dto.RewardCreateResponseDTO;
import com.example.testypie.domain.reward.dto.RewardDeleteResponseDTO;
import com.example.testypie.domain.reward.dto.RewardReadResponseDTO;
import com.example.testypie.domain.reward.service.RewardService;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.service.UserInfoService;
import com.example.testypie.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;
    private final UserInfoService userInfoService;

    @PostMapping("/reward")
    public ResponseEntity<RewardCreateResponseDTO> createReward(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody RewardCreateRequestDTO req
    ) {
        RewardCreateResponseDTO res = rewardService.createReward(req, userDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @GetMapping("/reward")
    public ResponseEntity<List<RewardReadResponseDTO>> getRewardList(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<RewardReadResponseDTO> resList = rewardService.getRewardList(userDetails.getUser());
        return ResponseEntity.ok(resList);
    }

    @GetMapping("/reward/{account}")
    public ResponseEntity<List<RewardReadResponseDTO>> getReward(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable String account) {

        userInfoService.checkSameUser(account, userDetails.getUsername());
        List<RewardReadResponseDTO> resList = rewardService.getReward(account);
        return ResponseEntity.ok(resList);
    }

    @DeleteMapping("/reward/{account}/{reward_id}")
    public ResponseEntity<RewardDeleteResponseDTO> deleteReward(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long reward_id, @PathVariable String account) {
        userInfoService.checkSameUser(account, userDetails.getUsername());
        User user = userDetails.getUser();
        RewardDeleteResponseDTO res = rewardService.deleteReward(user, reward_id);
        return ResponseEntity.ok().body(res);
    }
}