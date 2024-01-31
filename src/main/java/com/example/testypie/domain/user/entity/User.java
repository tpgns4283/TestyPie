package com.example.testypie.domain.user.entity;

import com.example.testypie.domain.reward.entity.Reward;
import com.example.testypie.domain.user.dto.ProfileRequestDTO;
import com.example.testypie.domain.userrole.constant.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String account;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column
    private String description;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private Long kakaoId;

    @OneToMany
    @JoinColumn
    private List<Reward> rewardList = new ArrayList<>();

    @Column
    private String fileUrl;

    @Builder
    private User(Long id, String account, String password, String email, String nickname,
                 String description, UserRole userRole, String fileUrl, Long kakaoId) {

        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.description = description;
        this.userRole = userRole;
        this.fileUrl = fileUrl;
        this.kakaoId = kakaoId;
    }

    public void update(ProfileRequestDTO req, String fileUrl, String password) {
        if (this.password != null && !this.password.isEmpty())
            this.password = password;
        if (req.nickname() != null && !req.nickname().isEmpty())
            this.nickname = req.nickname();
        if (req.description() != null && !req.description().isEmpty())
            this.description = req.description();
        this.fileUrl = fileUrl;
    }

    public User kakaoIdUpdate(Long kakaoId) {
        if(kakaoId != null)
            this.kakaoId = kakaoId;
        return this;
    }
}
