package com.example.testypie.domain.reward.service;

import com.example.testypie.domain.product.entity.Product;
import com.example.testypie.domain.product.service.ProductService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.RejectedExecutionException;

@Service
public class RewardService {

    private final RewardRepository rewardRepository;
    private final UserInfoService userInfoService;
    private final UserRepository userRepository;


    @Autowired
    public RewardService(RewardRepository rewardRepository, UserInfoService userInfoService, UserRepository userRepository) {
        this.rewardRepository = rewardRepository;
        this.userInfoService = userInfoService;
        this.userRepository = userRepository;
    }

    public RewardCreateResponseDTO createReward(RewardCreateRequestDTO req, UserDetailsImpl userDetails) {

        User user = userInfoService.findProfile(userDetails.getUser().getAccount());

        Reward saveReward = null;

            Reward reward = Reward.builder().reward_item(req.reward_item()).item_size(req.item_size()).user(user).build();
            saveReward = rewardRepository.save(reward);

        return RewardCreateResponseDTO.of(saveReward);
    }

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
        if(!user.getId().equals(reward.getUser().getId())) {
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
