package com.example.iticket.service.impl;

import com.example.iticket.dao.repository.UserRepository;
import com.example.iticket.exception.NotFoundException;
import com.example.iticket.service.concret.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email){
        log.info("ActionLog.loadUserByUsername.start email: {}", email);
        var userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        User user = new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getRoles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );
        log.info("ActionLog.loadUserByUsername.end email: {}", email);
        return user;
    }
}
