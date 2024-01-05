package com.example.testypie.user.entity;

import com.example.testypie.userrole.entity.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String account;

    @Column
    private String password;

    @Column
    @Email
    private String email;

    @Column
    private String nickname;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Builder
    private User(Long id, String account, String password, String email, String nickname,
        String description, UserRole userRole) {

        this.id = id;
        this.account = account;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.description = description;
        this.userRole = userRole;
    }
}
