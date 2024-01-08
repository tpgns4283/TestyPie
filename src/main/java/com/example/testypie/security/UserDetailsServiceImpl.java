package com.example.testypie.security;

import com.example.testypie.user.entity.User;
import com.example.testypie.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl {

    private final UserRepository userRepository;

    public UserDetailsImpl getUserDetails(String account) {
        User user = userRepository.findByAccount(account)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found" + account));
        return new UserDetailsImpl(user);
    }
}