package com.example.testypie.domain.reward.service;

import com.example.testypie.domain.reward.dto.RewardCreateRequestDTO;
import com.example.testypie.domain.reward.dto.RewardCreateResponseDTO;
import com.example.testypie.domain.reward.dto.RewardDeleteResponseDTO;
import com.example.testypie.domain.reward.dto.RewardReadResponseDTO;
import com.example.testypie.domain.reward.entity.Reward;
import com.example.testypie.domain.reward.repository.RewardRepository;
import com.example.testypie.domain.user.entity.User;
import com.example.testypie.domain.user.repository.UserRepository;
import com.example.testypie.domain.user.service.UserInfoService;
import com.example.testypie.global.security.UserDetailsImpl;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.RejectedExecutionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RewardService {

    private final RewardRepository rewardRepository;
    private final UserInfoService userInfoService;
    private final UserRepository userRepository;

    public List<RewardReadResponseDTO> getReward(String account) {
        User profileUser = userRepository.findByAccount(account)
                .orElseThrow(NoSuchElementException::new);

        return rewardRepository.findAllByUser(profileUser)
                .stream().map(RewardReadResponseDTO::new).toList();
    }

    public List<RewardReadResponseDTO> getRewardList(User user) {
        userInfoService.findProfile(user.getAccount());

        return rewardRepository.findAll()
                .stream().map(RewardReadResponseDTO::new).toList();

    }

    public RewardDeleteResponseDTO deleteReward(User user, Long reward_Id) {

        Reward reward = getUserReward(user, reward_Id);
        rewardRepository.delete(reward);

        return RewardDeleteResponseDTO.of(reward);
    }

    private Reward getUserReward(User user, Long rewardId) {
        Reward reward = findReward(rewardId);
        if (!user.getId().equals(reward.getUser().getId())) {
            throw new RejectedExecutionException("본인만 수정할 수 있습니다.");
        }
        return reward;
    }

    public Reward findReward(Long reward_Id) {
        return rewardRepository.findById(reward_Id).orElseThrow(
                () -> new IllegalArgumentException("reward id")
        );
    }
}
