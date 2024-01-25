package com.example.testypie.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class RefreshToken {
    @Id
    Long userId;
    String refreshToken;

    @Builder
    public RefreshToken(Long userId,String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
